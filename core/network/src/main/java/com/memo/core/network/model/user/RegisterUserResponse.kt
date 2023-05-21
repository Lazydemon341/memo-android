package com.memo.core.network.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RegisterUserResponse(
    @Json(name = "registered")
    val registered: Boolean,

    @Json(name = "tokens")
    val tokens: UserTokensResponse,
)
