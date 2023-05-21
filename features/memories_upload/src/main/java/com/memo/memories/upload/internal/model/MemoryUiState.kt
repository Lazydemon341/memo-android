package com.memo.memories.upload.internal.model

import android.net.Uri
import androidx.compose.runtime.Stable
import com.memo.core.model.Location
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point

internal data class MemoryUiState(
    val memoryId: Long,
    val title: String, // place
    val description: String, // dates
    val mapSection: MapSectionUiState,
    val gallerySection: GallerySectionUiState,
) {

    companion object {
        val EMPTY = MemoryUiState(
            0L,
            "",
            "",
            MapSectionUiState.EMPTY,
            GallerySectionUiState.EMPTY
        )
    }
}

@Stable
internal data class GallerySectionUiState(
    val images: List<PhotoUiState>,
) {

    companion object {
        val EMPTY = GallerySectionUiState(emptyList())
    }
}

@Stable
internal data class MapSectionUiState(
    val boundingBox: BoundingBox,
    val photos: List<PhotoUiState>
) {
    val shouldShowMap = this.photos.isNotEmpty()

    companion object {
        val EMPTY = MapSectionUiState(
            boundingBox = BoundingBox(
                Point(.0, .0),
                Point(.0, .0)
            ),
            emptyList()
        )
    }
}

internal data class PhotoUiState(
    val photoUri: Uri,
    val description: String,
    val timestamp: Long?,
    val photoLocation: Location?,
) {
    val shouldShowDescription = description.isNotBlank()
}
