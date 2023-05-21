package com.memo.core.network.retrofit

import com.memo.core.network.NetworkMapDataSource
import com.memo.core.network.model.map.MapRequest
import com.memo.core.network.model.map.MapResponse
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface MapApi {

    @POST("map/friends_photos")
    suspend fun getPhotos(@Body requestBody: MapRequest): MapResponse
}

@Singleton
class RetrofitMapDataSource @Inject constructor(
    apiFactory: ApiFactory,
) : NetworkMapDataSource {

    private val api = apiFactory.create(MapApi::class.java)

    override suspend fun getPhotos(request: MapRequest): MapResponse {
        return api.getPhotos(request)
    }
}
