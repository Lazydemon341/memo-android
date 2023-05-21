package com.memo.core.network.model.friendship

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RequestFriendshipBody(
    @Json(name = "user_id")
    val userId: Long,
)
