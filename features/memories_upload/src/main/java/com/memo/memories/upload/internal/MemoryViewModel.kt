package com.memo.memories.upload.internal

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.memo.core.data.repository.memories.GeneratedMemoriesRepository
import com.memo.core.model.memories.GeneratedMemory
import com.memo.core.model.memories.MemoryPhoto
import com.memo.core.utils.trySuspend
import com.memo.features.memories.upload.R
import com.memo.memories.upload.internal.data.PublishMemoryRepository
import com.memo.memories.upload.internal.model.GallerySectionUiState
import com.memo.memories.upload.internal.model.MapSectionUiState
import com.memo.memories.upload.internal.model.MemoryLoadingUiState
import com.memo.memories.upload.internal.model.MemoryUiState
import com.memo.memories.upload.internal.model.PhotoUiState
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MemoryViewModel @Inject constructor(
    private val memoriesRepository: GeneratedMemoriesRepository,
    private val publishMemoryRepository: PublishMemoryRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val mutableMemoryUiState = MutableStateFlow(MemoryUiState.EMPTY)
    val memoryUiState: StateFlow<MemoryUiState> = mutableMemoryUiState.asStateFlow()

    private val mutableMemoryLoadingState: MutableStateFlow<MemoryLoadingUiState> = MutableStateFlow(MemoryLoadingUiState.None)
    val memoryLoadingState = mutableMemoryLoadingState.asStateFlow()

    fun uploadMemory(memoryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableMemoryLoadingState.value = MemoryLoadingUiState.Loading
            val result = publishMemoryRepository.publishMemory(memoryId)
            when {
                result.isFailure -> {
                    mutableMemoryLoadingState.value = MemoryLoadingUiState.Error(
                        getErrorMessageResId(result.exceptionOrNull())
                    )
                }
                result.isSuccess -> {
                    mutableMemoryLoadingState.value = MemoryLoadingUiState.Success
                }
            }
        }
    }

    fun loadMemoryWithId(memoryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            trySuspend(
                run = {
                    val foundMemory = memoriesRepository.getMemoryById(memoryId)
                    foundMemory?.let {
                        mutableMemoryUiState.value = it.convertToUiState()
                    }
                },
                catch = {
                }
            )
        }
    }

    private fun GeneratedMemory.convertToUiState(): MemoryUiState {
        return MemoryUiState(
            memoryId = memoryId,
            title = title,
            description = caption,
            mapSection = getMapSectionUiState(photos),
            gallerySection =
            GallerySectionUiState(
                images = photos.map {
                    it.toUiState()
                }
            ),
        )
    }

    private fun MemoryPhoto.toUiState(): PhotoUiState {
        return PhotoUiState(
            photoUri = Uri.parse(photoUri),
            description = tags.joinToString(separator = " ") { "#$it" },
            timestamp = timestamp,
            photoLocation = photoLocation,
        )
    }

    private fun getMapSectionUiState(photos: List<MemoryPhoto>): MapSectionUiState {
        val locations = photos.mapNotNull { it.photoLocation }
        if (locations.isEmpty()) {
            return MapSectionUiState.EMPTY
        }
        return MapSectionUiState(
            boundingBox = getBoundingBox(photos),
            photos.map { it.toUiState() }
        )
    }

    private fun getBoundingBox(photos: List<MemoryPhoto>): BoundingBox {
        return photos.mapNotNull {
            it.photoLocation?.let { location ->
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

    private fun getErrorMessageResId(error: Throwable?): Int {
        return R.string.upload_error_message
    }
}
