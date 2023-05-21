package com.memo.chats.list.api.domain

import com.memo.core.model.messaging.Message

class Chat(
    val photoPreviewUrl: String?,
    val chatId: Long,
    val title: String,
    val lastMessage: Message?,
    val interlocutorEmail: String,
)
