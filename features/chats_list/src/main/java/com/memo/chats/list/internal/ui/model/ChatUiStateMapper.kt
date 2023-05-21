package com.memo.chats.list.internal.ui.model

import com.memo.chats.list.api.domain.Chat
import com.memo.core.model.messaging.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

internal class ChatUiStateMapper @Inject constructor() {

    fun map(chats: List<Chat>): List<ChatUiState> {
        return chats.map {
            mapChat(it)
        }
    }

    private fun mapChat(chat: Chat): ChatUiState {
        return ChatUiState(
            title = chat.title,
            id = chat.chatId,
            photoUrl = chat.photoPreviewUrl.orEmpty(),
            lastMessage = mapLastMessage(chat.lastMessage),
            interlocutorEmail = chat.interlocutorEmail,
        )
    }

    private fun mapLastMessage(message: Message?): LastMessageUiState {
        return LastMessageUiState(
            content = message?.text.orEmpty(),
            sentTime = formatMessageTime(message?.sentTimeMillis),
        )
    }

    private fun formatMessageTime(timestamp: Long?): String {
        if (timestamp == null) {
            return ""
        }
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }
}
