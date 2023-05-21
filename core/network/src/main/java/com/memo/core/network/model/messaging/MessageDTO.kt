package com.memo.core.network.model.messaging

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MessageDTO(
    @Json(name = "sent_ts")
    val sentTimestamp: Long,

    @Json(name = "sender")
    val sender: MessageSenderDTO,

    @Json(name = "text")
    val text: String,

    @Json(name = "message_id")
    val messageId: Long,
)
