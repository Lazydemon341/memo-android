package com.memo.memories.internal.ui.model

import androidx.compose.runtime.Stable
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point

internal data class MemorySuccessUiState(
    val title: String,
    val description: String,
    // val users: UsersUiState,
    val mainPhotos: List<PostUiState>,
    val galleryPhotos: List<GalleryImageUiState>,
    val mapUiState: MapSectionUiState,
) {
    val shouldShowDescription: Boolean
        get() = description.isNotEmpty()
}

internal data class UsersUiState(
    val users: List<UserUiState>,
    val description: String,
) {
    val shouldShowUsers: Boolean
        get() = users.isNotEmpty()

    val shouldShowDescription: Boolean
        get() = description.isNotEmpty()
}

internal data class PostUiState(
    val user: UserUiState,
    val with: List<Unit>,
    val imageUrl: String,
    val description: String,
    val likedByUser: Boolean,
    val likesCount: Int,
    val commentsCount: Int,
    val location: String,
)

internal data class GalleryImageUiState(
    val imageUrl: String,
    val liked: Boolean,
)

internal data class UserUiState(
    val image: Unit,
    val username: String,
)

@Stable
internal data class MapSectionUiState(
    val boundingBox: BoundingBox,
    val photos: List<PhotoOnMapUiState>
) {
    val shouldShowMap = this.photos.isNotEmpty()

    companion object {
        val EMPTY = MapSectionUiState(
            boundingBox = BoundingBox(
                Point(.0, .0),
                Point(.0, .0)
            ),
            photos = emptyList(),
        )
    }
}

internal data class PhotoOnMapUiState(
    val photoUrl: String,
    val photoPoint: Point,
)
