package com.memo.core.network.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserIdParams(
    @Json(name = "email")
    val email: String,
)
