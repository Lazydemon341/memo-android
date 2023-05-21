package com.memo.core.data.repository.map

import com.memo.core.data.mappers.toPhotoOnMap
import com.memo.core.model.map.Period
import com.memo.core.model.map.PhotoOnMap
import com.memo.core.network.NetworkMapDataSource
import com.memo.core.network.model.map.MapRequest
import com.memo.core.network.model.map.PeriodDTO
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val dataSource: NetworkMapDataSource
) : MapRepository {

    override suspend fun getPhotos(requestPeriod: Period?): Result<List<PhotoOnMap>> = withContext(Dispatchers.IO) {
        suspendRunCatching {
            val request = MapRequest(
                period = requestPeriod?.let { PeriodDTO(start = it.start, end = it.end) }
            )
            retryOnNetworkOrServerErrors {
                dataSource.getPhotos(request)
            }.annotations.map {
                it.toPhotoOnMap()
            }
        }
    }
}
