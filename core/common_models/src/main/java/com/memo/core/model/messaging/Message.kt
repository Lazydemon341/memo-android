package com.memo.core.model.messaging

class Message(
    val id: Long,
    val sentTimeMillis: Long,
    val sender: MessageSender,
    val text: String,
)
