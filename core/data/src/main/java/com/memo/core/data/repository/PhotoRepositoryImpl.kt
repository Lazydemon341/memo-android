package com.memo.core.data.repository

import android.graphics.Bitmap
import com.memo.core.storage.SessionBitmapFileStorage
import com.memo.core.utils.suspendRunCatching
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val sessionBitmapFileStorage: SessionBitmapFileStorage,
) : PhotoRepository {

    override suspend fun setPhoto(photo: Bitmap): Result<Unit> {
        return suspendRunCatching {
            sessionBitmapFileStorage.writeToFile(photo)
        }
    }
}
