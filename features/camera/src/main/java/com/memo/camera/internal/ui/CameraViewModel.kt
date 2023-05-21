package com.memo.camera.internal.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.memo.core.data.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class CameraViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
) : ViewModel() {

    suspend fun onPhotoTaken(photo: Bitmap) {
        photoRepository.setPhoto(photo)
    }
}
