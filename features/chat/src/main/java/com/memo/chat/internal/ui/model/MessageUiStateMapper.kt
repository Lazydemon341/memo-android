package com.memo.chat.internal.ui.model

import com.memo.core.model.messaging.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

internal class MessageUiStateMapper @Inject constructor() {
    fun mapMessage(message: Message, interlocutorEmail: String): MessageUiState {
        return MessageUiState(
            isByUser = interlocutorEmail != message.sender.email,
            content = message.text,
            id = message.id,
            sentDate = formatDate(message.sentTimeMillis),
            sentTime = formatTime(message.sentTimeMillis),
            authorImageUrl = message.sender.photoUrl,
        )
    }

    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }

    private fun formatTime(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }
}
