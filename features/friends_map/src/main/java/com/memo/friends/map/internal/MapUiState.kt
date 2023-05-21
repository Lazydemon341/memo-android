package com.memo.friends.map.internal

import androidx.compose.runtime.Stable
import com.memo.core.model.map.PhotoOnMap
import com.yandex.mapkit.geometry.Point

@Stable
internal data class MapUiState(
    val photos: List<PhotoOnMapUiState>,
    val cameraLocation: CameraLocation
)

internal data class PhotoOnMapUiState(
    val postId: Long,
    val photoUrl: String,
    val photoPoint: Point,
)

internal data class CameraLocation(val cameraPoint: Point, val zoom: Float)

internal fun PhotoOnMap.toPhotoOnMapUiState(): PhotoOnMapUiState {
    return PhotoOnMapUiState(
        postId = postId,
        photoUrl = photoUrl,
        photoPoint = Point(
            location.latitude.toDouble(),
            location.longitude.toDouble()
        )
    )
}
