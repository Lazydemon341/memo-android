package com.memo.core.network.model.messaging

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MessagesPollingResponse(
    @Json(name = "messages")
    val messages: List<MessageDTO>,
)
