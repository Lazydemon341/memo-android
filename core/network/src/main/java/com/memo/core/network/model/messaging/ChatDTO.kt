package com.memo.core.network.model.messaging

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ChatDTO(
    @Json(name = "photo_preview_url")
    val photoPreviewUrl: String?,

    @Json(name = "chat_id")
    val chatId: Long,

    @Json(name = "title")
    val title: String,

    @Json(name = "last_message")
    val lastMessage: MessageDTO?,

    @Json(name = "interlocutor_email")
    val interlocutorEmail: String,
)
