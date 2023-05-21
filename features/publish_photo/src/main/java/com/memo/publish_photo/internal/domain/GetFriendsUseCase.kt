package com.memo.publish_photo.internal.domain

import com.memo.core.model.friendship.Friend
import com.memo.publish_photo.internal.data.PublishPhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val repository: PublishPhotoRepository,
) {
    operator fun invoke(): Flow<List<Friend>> {
        return repository.friendsFlow()
            .onStart { emit(emptyList()) }
            .catch {
                Timber.e(it, "Error getting friends list")
                emit(emptyList())
            }
    }
}
