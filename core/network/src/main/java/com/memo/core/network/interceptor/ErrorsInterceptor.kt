package com.memo.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * OkHttp propagates only IOExceptions to the coroutine. Any other exception is thrown directly to the
 * UncaughtExceptionHandler, which crashes the app. Hence we need to catch it here
 * @see <a href="https://github.com/square/okhttp/issues/5151">GitHub issue</a>
 */
class ErrorsInterceptor @Inject constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return chain.proceed(chain.request())
        } catch (e: Throwable) {
            if (e is IOException) {
                throw e
            } else {
                throw IOException(e)
            }
        }
    }
}
