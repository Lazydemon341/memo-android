package com.memo.user.profile.internal.ui.model

import androidx.compose.runtime.Immutable

internal sealed interface UserProfileUiState {
    object Loading : UserProfileUiState

    object Error : UserProfileUiState

    @Immutable
    data class Ready(
        val isCurrentUserProfile: Boolean,
        val avatarUrl: String?,
        val name: String,
        val email: String,
        val friendsCount: Int,
        val memories: List<UserProfileMemoryUiState>,
    ) : UserProfileUiState
}
