package com.memo.core.network

import android.graphics.Bitmap
import com.memo.core.network.model.photo.UploadPhotoResponse
import java.io.File

interface NetworkPhotoDataSource {
    suspend fun uploadPhoto(photoFile: File): UploadPhotoResponse

    suspend fun getPhoto(id: Long): Bitmap
}
