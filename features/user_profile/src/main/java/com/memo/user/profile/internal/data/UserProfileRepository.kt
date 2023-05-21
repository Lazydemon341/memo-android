package com.memo.user.profile.internal.data

import android.graphics.Bitmap
import android.net.Uri
import com.memo.core.data.photos.ImagesProvider
import com.memo.core.storage.SessionBitmapFileStorage
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import com.memo.user.profile.internal.data.network.NetworkProfileDataSource
import com.memo.user.profile.internal.domain.model.UserProfile
import timber.log.Timber
import javax.inject.Inject

private const val AVATAR_SIZE = 320

internal class UserProfileRepository @Inject constructor(
    private val networkProfileDataSource: NetworkProfileDataSource,
    private val sessionBitmapFileStorage: SessionBitmapFileStorage,
    private val imagesProvider: ImagesProvider,
) {
    suspend fun getProfile(userId: Long?): Result<UserProfile> {
        return suspendRunCatching {
            val response = retryOnNetworkOrServerErrors {
                networkProfileDataSource.getProfileData(userId = userId)
            }
            response.toDomainModel()
        }
    }

    suspend fun changeAvatar(uri: Uri): Result<Unit> {
        return suspendRunCatching {
            loadImageIntoFileStorage(uri)
            val file = sessionBitmapFileStorage.acquireFile()
            retryOnNetworkOrServerErrors {
                networkProfileDataSource.changeAvatar(file)
            }
        }.also {
            sessionBitmapFileStorage.releaseFile()
        }.onFailure {
            Timber.e(it)
        }
    }

    private suspend fun loadImageIntoFileStorage(imageUri: Uri) {
        val bitmap = imagesProvider.getImageByUri(imageUri)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, AVATAR_SIZE, AVATAR_SIZE, false)
        sessionBitmapFileStorage.writeToFile(resizedBitmap)
    }
}
