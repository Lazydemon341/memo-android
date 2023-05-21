package com.memo.core.network.model.messaging

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ChatResponse(
    @Json(name = "title")
    val title: String,

    @Json(name = "messages")
    val messages: List<MessageDTO>,
)
