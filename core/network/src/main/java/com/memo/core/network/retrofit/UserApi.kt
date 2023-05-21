package com.memo.core.network.retrofit

import com.memo.core.network.NetworkUserDataSource
import com.memo.core.network.WithoutToken
import com.memo.core.network.model.user.AuthBody
import com.memo.core.network.model.user.ConfirmUserBody
import com.memo.core.network.model.user.ConfirmUserResponse
import com.memo.core.network.model.user.RefreshBody
import com.memo.core.network.model.user.RegisterUserBody
import com.memo.core.network.model.user.RegisterUserResponse
import com.memo.core.network.model.user.UserIdParams
import com.memo.core.network.model.user.UserIdResponse
import com.memo.core.network.model.user.UserTokensResponse
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface UserApi {
    @WithoutToken
    @POST("user/register")
    suspend fun register(@Body data: RegisterUserBody): RegisterUserResponse

    @POST("user/confirm")
    suspend fun confirm(@Body data: ConfirmUserBody): ConfirmUserResponse

    @WithoutToken
    @POST("user/auth")
    suspend fun auth(@Body data: AuthBody): UserTokensResponse

    @WithoutToken
    @POST("user/refresh")
    suspend fun refresh(@Body data: RefreshBody): UserTokensResponse

    @POST("user/user_id")
    suspend fun userId(@Body userIdParams: UserIdParams): UserIdResponse
}

@Singleton
class RetrofitUserDataSource @Inject constructor(
    apiFactory: ApiFactory,
) : NetworkUserDataSource {

    private val api = apiFactory.create(UserApi::class.java)

    override suspend fun register(data: RegisterUserBody): RegisterUserResponse {
        return api.register(data)
    }

    override suspend fun confirm(data: ConfirmUserBody): ConfirmUserResponse {
        return api.confirm(data)
    }

    override suspend fun auth(data: AuthBody): UserTokensResponse {
        return api.auth(data)
    }

    override suspend fun refresh(data: RefreshBody): UserTokensResponse {
        return api.refresh(data)
    }
}
