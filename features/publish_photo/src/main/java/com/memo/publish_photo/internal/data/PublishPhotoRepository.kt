package com.memo.publish_photo.internal.data

import android.graphics.Bitmap
import com.memo.core.data.mappers.toLocationDTO
import com.memo.core.data.repository.FriendshipRepository
import com.memo.core.model.Location
import com.memo.core.model.friendship.Friend
import com.memo.core.network.NetworkPhotoDataSource
import com.memo.core.network.NetworkSinglePostDataSource
import com.memo.core.network.model.post.PhotoDTO
import com.memo.core.network.model.post.PostUploadParams
import com.memo.core.storage.SessionBitmapFileStorage
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

class PublishPhotoRepository @Inject constructor(
    private val friendshipRepository: FriendshipRepository,
    private val networkPhotoDataSource: NetworkPhotoDataSource,
    private val networkPostDataSource: NetworkSinglePostDataSource,
    private val sessionBitmapFileStorage: SessionBitmapFileStorage,
) {
    fun friendsFlow(): Flow<List<Friend>> {
        return friendshipRepository.friendsFlow()
            .onStart { friendshipRepository.updateFriends().getOrThrow() }
    }

    suspend fun getPhoto(): Result<Bitmap> {
        return suspendRunCatching {
            sessionBitmapFileStorage.readAsBitmap()
        }
    }

    suspend fun publishPhoto(location: Location?): Result<Unit> {
        return suspendRunCatching {
            val photoFile = sessionBitmapFileStorage.acquireFile()

            val photoUploadResponse = retryOnNetworkOrServerErrors {
                networkPhotoDataSource.uploadPhoto(photoFile)
            }

            val photoId = photoUploadResponse.photosIds.firstOrNull()
                ?: throw IllegalStateException("No photo id")

            val postUploadParams = PostUploadParams(
                photo = PhotoDTO(
                    photoId = photoId,
                    location = location?.toLocationDTO(),
                ),
                memoryId = null,
                caption = null,
                peopleToSend = null,
            )

            retryOnNetworkOrServerErrors {
                networkPostDataSource.upload(postUploadParams)
            }
        }.also {
            sessionBitmapFileStorage.releaseFile()
        }.onFailure {
            Timber.e(it, "Failed to upload post")
        }
    }
}
