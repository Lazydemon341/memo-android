package com.memo.core.network.interceptor

import com.memo.core.datastore.user_tokens.UserTokens
import com.memo.core.datastore.user_tokens.UserTokensDataSource
import com.memo.core.network.NetworkUserDataSource
import com.memo.core.network.WithoutToken
import com.memo.core.network.model.user.RefreshBody
import com.memo.core.network.model.user.toLocalModel
import com.memo.core.utils.suspendRunCatching
import dagger.Lazy
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val userTokensDataSource: UserTokensDataSource,
    private val networkUserDataSourceProvider: Lazy<NetworkUserDataSource>,
) : Interceptor {

    private val lock = Any()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val method = request.tag(Invocation::class.java)?.method()
        if (method?.isAnnotationPresent(WithoutToken::class.java) == true) {
            // No token needed
            return chain.proceed(request)
        }

        var accessToken: String? = getLocalTokens()?.accessToken

        // Try request with current token
        val response = chain.proceed(request.withAccessToken(accessToken))

        if (!request.isRetry() && response.code == 403) {
            // Token is not valid
            synchronized(lock) {
                val localTokens = getLocalTokens()
                if (localTokens?.accessToken != accessToken) {
                    // Tokens already refreshed
                    response.close()
                    accessToken = localTokens?.accessToken
                    return chain.proceed(request.withAccessToken(accessToken).markAsRetry())
                } else {
                    accessToken = refreshTokens(localTokens)
                    if (accessToken.isNullOrBlank()) {
                        // Failed to refresh tokens
                        // TODO: navigate to login screen
                        return response
                    }
                    response.close()
                    return chain.proceed(request.withAccessToken(accessToken).markAsRetry())
                }
            }
        }

        return response
    }

    private fun getLocalTokens(): UserTokens? = runBlocking {
        userTokensDataSource.tokensFlow.firstOrNull()
    }

    private fun refreshTokens(localTokens: UserTokens?): String? = runBlocking {
        localTokens ?: return@runBlocking null

        val freshTokensResponse = suspendRunCatching {
            networkUserDataSourceProvider.get().refresh(RefreshBody(localTokens.refreshToken))
        }.getOrNull()
        userTokensDataSource.setTokens(freshTokensResponse?.toLocalModel())

        return@runBlocking freshTokensResponse?.accessToken
    }

    private fun Request.withAccessToken(accessToken: String?): Request {
        return newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }

    private object Retried

    private fun Request.isRetry(): Boolean {
        return tag(Retried::class.java) != null
    }

    private fun Request.markAsRetry(): Request {
        return newBuilder()
            .tag(Retried::class.java, Retried)
            .build()
    }
}
