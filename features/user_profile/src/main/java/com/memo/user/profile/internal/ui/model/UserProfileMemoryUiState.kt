package com.memo.user.profile.internal.ui.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class UserProfileMemoryUiState(
    val id: Long,
    val name: String,
    val photoPreviewUrl: String,
)
