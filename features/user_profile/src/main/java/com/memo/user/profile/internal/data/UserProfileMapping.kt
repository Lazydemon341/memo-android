package com.memo.user.profile.internal.data

import com.memo.user.profile.internal.data.network.model.ProfileDataResponse
import com.memo.user.profile.internal.data.network.model.ProfileMemoryDTO
import com.memo.user.profile.internal.domain.model.UserProfile
import com.memo.user.profile.internal.domain.model.UserProfileMemory

internal fun ProfileDataResponse.toDomainModel(): UserProfile {
    return UserProfile(
        avatarUrl = avatarUrl,
        name = name,
        email = email,
        friendsCount = friendsCount,
        memories = memories.map { it.toDomainModel() }
    )
}

internal fun ProfileMemoryDTO.toDomainModel(): UserProfileMemory {
    return UserProfileMemory(
        id = id,
        name = name,
        photoPreviewUrl = photoPreviewUrl,
    )
}
