package com.memo.core.network

import com.memo.core.network.model.map.MapRequest
import com.memo.core.network.model.map.MapResponse

interface NetworkMapDataSource {

    suspend fun getPhotos(request: MapRequest): MapResponse
}
