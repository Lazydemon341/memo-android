package com.memo.core.data.repository

import android.graphics.Bitmap

interface PhotoRepository {
    suspend fun setPhoto(photo: Bitmap): Result<Unit>
}
