package com.memo.core.network.model.messaging

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SendMessageParams(
    @Json(name = "text")
    val text: String,

    @Json(name = "chat_id")
    val chatId: Long,
)
