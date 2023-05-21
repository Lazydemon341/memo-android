package com.memo.publish_photo.internal.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap

@Immutable
internal sealed class PublishPhotoUiState {

    internal object Loading : PublishPhotoUiState()

    @Immutable
    internal data class Ready(
        val photo: ImageBitmap,
        val friends: List<FriendUiState>,
        val locationUiState: LocationUiState,
    ) : PublishPhotoUiState()

    internal object Error : PublishPhotoUiState()
}

@Immutable
internal data class FriendUiState(
    val id: Long,
    val imageUrl: String,
    val name: String,
    val isSelected: Boolean,
)

@Immutable
internal sealed class LocationUiState {

    @Immutable
    internal data class Enabled(
        val location: String,
        val enabled: Boolean,
    ) : LocationUiState()

    internal object Empty : LocationUiState()
}
