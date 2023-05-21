package com.memo.memories.internal.ui.model

import com.memo.core.location.LocationProvider
import com.memo.memories.internal.data.network.model.MemoryPhotoDTO
import com.memo.memories.internal.data.network.model.MemoryResponse
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

private const val mainPhotosSize = 3

internal class MemoryUiStateMapper @Inject constructor(
    private val locationProvider: LocationProvider,
) {

    suspend fun map(memory: MemoryResponse): MemorySuccessUiState {
        return coroutineScope {
            val galleryPhotos = async { mapGalleryPhotos(memory.photos) }
            val mapUiState = async { getMapSectionUiState(memory.photos) }
            MemorySuccessUiState(
                title = memory.name,
                description = memory.caption,
                mainPhotos = mapMainPhotos(memory.photos),
                galleryPhotos = galleryPhotos.await(),
                mapUiState = mapUiState.await(),
            )
        }
    }

    private suspend fun mapMainPhotos(photos: List<MemoryPhotoDTO>): List<PostUiState> {
        return coroutineScope {
            photos
                .take(mainPhotosSize)
                .map {
                    async {
                        val address = it.location?.let { location ->
                            locationProvider.getAddress(
                                latitude = location.latitude.toDouble(),
                                longitude = location.longitude.toDouble(),
                            )
                        }
                        PostUiState(
                            user = UserUiState(Unit, ""),
                            with = emptyList(),
                            description = it.caption.orEmpty(),
                            likedByUser = false,
                            likesCount = 0,
                            commentsCount = 0,
                            imageUrl = it.photoUrl,
                            location = address?.getAddressLine(0).orEmpty(),
                        )
                    }
                }
                .awaitAll()
        }
    }

    private fun mapGalleryPhotos(photos: List<MemoryPhotoDTO>): List<GalleryImageUiState> {
        return photos
            .drop(mainPhotosSize)
            .map {
                GalleryImageUiState(
                    imageUrl = it.photoUrl,
                    liked = false,
                )
            }
    }

    private fun getMapSectionUiState(photos: List<MemoryPhotoDTO>): MapSectionUiState {
        return MapSectionUiState(
            photos = photos.mapNotNull {
                it.location?.let { location ->
                    PhotoOnMapUiState(
                        photoUrl = it.photoUrl,
                        photoPoint = Point(
                            location.latitude.toDouble(),
                            location.longitude.toDouble(),
                        )
                    )
                }
            },
            boundingBox = getBoundingBox(photos),
        )
    }

    private fun getBoundingBox(photos: List<MemoryPhotoDTO>): BoundingBox {
        return photos.mapNotNull {
            it.location?.let { location ->
                Point(location.latitude.toDouble(), location.longitude.toDouble())
            }
        }.toBoundingBox()
    }

    private fun List<Point>.toBoundingBox(): BoundingBox {
        var mostSouth = first().latitude
        var mostWest = first().longitude
        var mostNorth = first().latitude
        var mostEast = first().longitude
        for (point in this) {
            if (point.latitude < mostSouth) {
                mostSouth = point.latitude
            }
            if (point.latitude > mostNorth) {
                mostNorth = point.latitude
            }
            if (point.longitude < mostWest) {
                mostWest = point.longitude
            }
            if (point.longitude > mostEast) {
                mostEast = point.longitude
            }
        }
        val southWest = Point(mostSouth, mostWest)
        val northEast = Point(mostNorth, mostEast)
        return BoundingBox(southWest, northEast)
    }
}
