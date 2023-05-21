package com.memo.core.network.model.user

import com.memo.core.datastore.user_tokens.UserTokens
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserTokensResponse(
    @Json(name = "access_token")
    val accessToken: String,

    @Json(name = "access_token_expire_time")
    val accessTokenExpireTime: Long,

    @Json(name = "refresh_token")
    val refreshToken: String,

    @Json(name = "refresh_token_expire_time")
    val refreshTokenExpireTime: Long,

    @Json(name = "user_id")
    val userId: Long?,
)

fun UserTokensResponse.toLocalModel(): UserTokens {
    return UserTokens(
        accessToken = accessToken,
        accessTokenExpireTime = accessTokenExpireTime,
        refreshToken = refreshToken,
        refreshTokenExpireTime = refreshTokenExpireTime,
        userId = userId ?: 0,
    )
}
