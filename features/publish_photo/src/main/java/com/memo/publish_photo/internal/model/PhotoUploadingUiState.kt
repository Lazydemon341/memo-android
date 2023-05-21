package com.memo.publish_photo.internal.model

import androidx.compose.runtime.Immutable

internal sealed class PhotoUploadingUiState {

    internal object None : PhotoUploadingUiState()

    internal object Loading : PhotoUploadingUiState()

    internal object Success : PhotoUploadingUiState()

    @Immutable
    internal data class Failure(val message: String? = null) : PhotoUploadingUiState()
}
