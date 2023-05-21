package com.memo.core.network.model.feed

import com.memo.core.network.model.post.PostDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedResponse(
    @Json(name = "posts")
    val posts: List<PostDTO>
)
