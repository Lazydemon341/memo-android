package com.memo.user.profile.internal.domain

import android.net.Uri
import com.memo.user.profile.internal.data.UserProfileRepository
import javax.inject.Inject

internal class ChangeAvatarUseCase @Inject constructor(
    private val profileRepository: UserProfileRepository,
) {
    suspend operator fun invoke(uri: Uri): Result<Unit> {
        return profileRepository.changeAvatar(uri)
    }
}
