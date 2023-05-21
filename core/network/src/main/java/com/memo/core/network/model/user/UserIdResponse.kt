package com.memo.core.network.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserIdResponse(
    @Json(name = "id")
    val id: Long,
)
