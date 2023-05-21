package com.memo.chats.list.internal.ui.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class ChatUiState(
    val title: String,
    val id: Long,
    val photoUrl: String,
    val lastMessage: LastMessageUiState,
    val interlocutorEmail: String,
)

@Immutable
internal data class LastMessageUiState(
    val content: String,
    val sentTime: String,
)
