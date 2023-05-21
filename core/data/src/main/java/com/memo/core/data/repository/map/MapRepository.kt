package com.memo.core.data.repository.map

import com.memo.core.model.map.Period
import com.memo.core.model.map.PhotoOnMap

interface MapRepository {

    suspend fun getPhotos(requestPeriod: Period?): Result<List<PhotoOnMap>>
}
