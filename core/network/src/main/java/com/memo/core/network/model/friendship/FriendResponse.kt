package com.memo.core.network.model.friendship

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FriendResponse(
    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,
)
