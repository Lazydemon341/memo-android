package com.memo.core.network.model.feed

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedRequest(
    @Json(name = "amount")
    val amount: Int,
    @Json(name = "oldest_post_id")
    val oldestPostId: Long?,
)
