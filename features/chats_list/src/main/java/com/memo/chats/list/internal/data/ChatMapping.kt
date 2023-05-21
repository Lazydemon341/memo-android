package com.memo.chats.list.internal.data

import com.memo.chats.list.api.domain.Chat
import com.memo.core.data.common_models.toDomainModel
import com.memo.core.network.model.messaging.ChatDTO

internal fun ChatDTO.toDomain(): Chat {
    return Chat(
        photoPreviewUrl = photoPreviewUrl,
        chatId = chatId,
        title = title,
        lastMessage = lastMessage?.toDomainModel(),
        interlocutorEmail = interlocutorEmail,
    )
}
