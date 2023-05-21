package com.memo.user.profile.internal.domain

import com.memo.user.profile.internal.data.UserProfileRepository
import com.memo.user.profile.internal.domain.model.UserProfile
import javax.inject.Inject

internal class GetProfileUseCase @Inject constructor(
    private val profileRepository: UserProfileRepository,
) {
    suspend operator fun invoke(userId: Long?): Result<UserProfile> {
        return profileRepository.getProfile(userId = userId)
    }
}
