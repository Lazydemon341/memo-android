package com.memo.chat.internal.ui.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class MessageUiState(
    val isByUser: Boolean,
    val content: String,
    val id: Long,
    val sentDate: String,
    val sentTime: String,
    val authorImageUrl: String?,
)
