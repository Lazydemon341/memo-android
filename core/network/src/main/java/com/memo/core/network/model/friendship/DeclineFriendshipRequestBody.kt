package com.memo.core.network.model.friendship

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DeclineFriendshipRequestBody(
    @Json(name = "request_id")
    val requestId: Long,
)
