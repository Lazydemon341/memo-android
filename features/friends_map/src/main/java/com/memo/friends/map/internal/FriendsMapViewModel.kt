package com.memo.friends.map.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.core.data.repository.map.MapRepository
import com.memo.core.model.map.Period
import com.memo.friends.map.internal.FilterType.FOR_TODAY
import com.memo.friends.map.internal.FilterType.FOR_WEEK
import com.memo.friends.map.internal.FilterType.FOR_YESTERDAY
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel
internal class FriendsMapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {

    private val mutableFilterTypeFlow: MutableStateFlow<FilterType> = MutableStateFlow(FOR_WEEK)
    val filterTypeFlow = mutableFilterTypeFlow.asStateFlow()

    private val mutableMapUiState: MutableStateFlow<MapUiState> = MutableStateFlow(
        MapUiState(
            photos = emptyList(),
            cameraLocation = CameraLocation(Point(55.751244, 37.618423), 10f)
        )
    )

    val mapUiState = mutableMapUiState.asStateFlow()

    fun requestPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            val (start, end) = getStartAndEndTimestamps()
            val result = mapRepository.getPhotos(Period(start / 1000, end / 1000))
            val data = result.getOrNull()
            if (data != null) {
                val photosUiState = data.map {
                    it.toPhotoOnMapUiState()
                }
                mutableMapUiState.value = MapUiState(
                    photos = photosUiState,
                    cameraLocation = CameraLocation(Point(55.751244, 37.618423), 10f)
                )
            }
        }
    }

    fun setFilteringType(type: FilterType) {
        mutableFilterTypeFlow.value = type
        requestPhotos()
    }

    private fun getStartAndEndTimestamps(): Pair<Long, Long> {
        return when (filterTypeFlow.value) {
            FOR_TODAY -> {
                val currentDate = Date(System.currentTimeMillis())
                val start = currentDate.atStartOfDay()
                val end = currentDate.atEndOfDay()
                start.time to end.time
            }
            FOR_YESTERDAY -> {
                val currentDate = Date(System.currentTimeMillis() - MILLIS_IN_DAY)
                val start = currentDate.atStartOfDay()
                val end = currentDate.atEndOfDay()
                start.time to end.time
            }
            FOR_WEEK -> {
                val end = System.currentTimeMillis()
                val start = end - MILLIS_IN_WEEK
                start to end
            }
        }
    }

    private fun Date.atStartOfDay(): Date {
        val localDateTime = dateToLocalDateTime()
        val startOfDay = localDateTime.with(LocalTime.MIN)
        return localDateTimeToDate(startOfDay)
    }

    private fun Date.atEndOfDay(): Date {
        val localDateTime = dateToLocalDateTime()
        val endOfDay = localDateTime.with(LocalTime.MAX)
        return localDateTimeToDate(endOfDay)
    }

    private fun Date.dateToLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault())
    }

    private fun localDateTimeToDate(localDateTime: LocalDateTime): Date {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    private companion object {
        private const val MILLIS_IN_DAY = 1000L * 60 * 60 * 24
        private const val MILLIS_IN_WEEK = MILLIS_IN_DAY * 7
    }
}

internal enum class FilterType {
    FOR_TODAY, FOR_YESTERDAY, FOR_WEEK,
}
