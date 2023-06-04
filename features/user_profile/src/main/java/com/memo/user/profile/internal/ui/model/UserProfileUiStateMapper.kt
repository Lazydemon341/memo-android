package com.memo.user.profile.internal.ui.model

import com.memo.user.profile.internal.domain.model.UserProfile
import com.memo.user.profile.internal.domain.model.UserProfileMemory
import javax.inject.Inject

internal class UserProfileUiStateMapper @Inject constructor() {
    fun map(profile: UserProfile, isCurrentUser: Boolean): UserProfileUiState {
        return UserProfileUiState.Ready(
            isCurrentUserProfile = isCurrentUser,
            avatarUrl = profile.avatarUrl,
            name = profile.name,
            email = profile.email,
            friendsCount = profile.friendsCount,
            memories = mapMemories(profile.memories),
        )
    }

    private fun mapMemories(memories: List<UserProfileMemory>): List<UserProfileMemoryUiState> {
        return memories.map { memory ->
            UserProfileMemoryUiState(
                id = memory.id,
                name = memory.name,
                photoPreviewUrl = memory.photoPreviewUrl,
            )
        }
    }
}
