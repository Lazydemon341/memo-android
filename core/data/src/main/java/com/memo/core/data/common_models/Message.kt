package com.memo.core.data.common_models

import com.memo.core.model.messaging.Message
import com.memo.core.model.messaging.MessageSender
import com.memo.core.network.model.messaging.MessageDTO
import com.memo.core.network.model.messaging.MessageSenderDTO

fun MessageDTO.toDomainModel(): Message {
    return Message(
        id = messageId,
        sentTimeMillis = sentTimestamp.times(1000L),
        sender = sender.toDomainModel(),
        text = text,
    )
}

fun MessageSenderDTO.toDomainModel(): MessageSender {
    return MessageSender(
        email = email,
        photoUrl = photoUrl,
    )
}
