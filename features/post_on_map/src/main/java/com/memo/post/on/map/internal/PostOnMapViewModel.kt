package com.memo.post.on.map.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.core.data.repository.post.SinglePostRepository
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType.COMBINED
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PostOnMapViewModel @Inject constructor(
    private val singlePostRepository: SinglePostRepository,
) : ViewModel() {

    private val mutablePostUiStateFlow: MutableStateFlow<PostUiState?> = MutableStateFlow(null)
    val postUiStateFlow: StateFlow<PostUiState?> = mutablePostUiStateFlow.asStateFlow()

    private val mutableNearByPlaces: MutableStateFlow<PlacesUiState?> = MutableStateFlow(null)
    val nearByPlaces = mutableNearByPlaces.asStateFlow()

    fun getPostById(postId: Long) {
        viewModelScope.launch {
            val result = singlePostRepository.getPostById(postId)
            val postUiState = result.getOrNull()?.toPostUiState()
            mutablePostUiStateFlow.value = postUiState
            postUiState?.location?.let {
                getNearByPlaces(Point(it.latitude.toDouble(), it.longitude.toDouble()))
            }
        }
    }

    private fun getNearByPlaces(point: Point) {
        val searchManager = SearchFactory.getInstance().createSearchManager(COMBINED)
        searchManager.submit(
            QUERY_STRING,
            Geometry.fromBoundingBox(createBoundingBoxForPoint(point)),
            SearchOptions(),
            MapSearchListener()
        )
    }

    private inner class MapSearchListener : Session.SearchListener {
        override fun onSearchResponse(p0: Response) {
            val places = p0.collection.children.mapNotNull {
                it.obj?.toPlace()
            }
            mutableNearByPlaces.value = PlacesUiState(places)
        }

        override fun onSearchError(p0: Error) {
            mutableNearByPlaces.value = null
        }
    }

    private fun createBoundingBoxForPoint(point: Point): BoundingBox {
        val southWestPoint =
            Point(point.latitude - BOUNDING_BOX_DIF, point.longitude - BOUNDING_BOX_DIF)
        val northEastPoint =
            Point(point.latitude + BOUNDING_BOX_DIF, point.longitude + BOUNDING_BOX_DIF)
        return BoundingBox(southWestPoint, northEastPoint)
    }

    private fun GeoObject.toPlace(): Place {
        return Place(
            name ?: "",
            descriptionText ?: ""
        )
    }

    private companion object {
        private const val QUERY_STRING = "Что посетить"
        private const val BOUNDING_BOX_DIF = 0.001
    }
}
