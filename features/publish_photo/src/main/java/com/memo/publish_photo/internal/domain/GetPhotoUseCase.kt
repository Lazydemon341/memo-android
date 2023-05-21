package com.memo.publish_photo.internal.domain

import android.graphics.Bitmap
import com.memo.publish_photo.internal.data.PublishPhotoRepository
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(
    private val repository: PublishPhotoRepository,
) {
    suspend operator fun invoke(): Result<Bitmap> {
        return repository.getPhoto()
    }
}
