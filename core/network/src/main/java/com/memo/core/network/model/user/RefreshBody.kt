package com.memo.core.network.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RefreshBody(
    @Json(name = "refresh_token")
    val refreshToken: String,
)
