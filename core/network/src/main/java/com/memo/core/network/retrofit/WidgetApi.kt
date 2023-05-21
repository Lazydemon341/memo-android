package com.memo.core.network.retrofit

import com.memo.core.network.NetworkWidgetDataSource
import com.memo.core.network.model.widgets.PhotoWidgetResponse
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

interface WidgetApi {

    @GET("widget")
    suspend fun getLastPhoto(): PhotoWidgetResponse
}

@Singleton
class RetrofitWidgetDataSource @Inject constructor(
    apiFactory: ApiFactory,
) : NetworkWidgetDataSource {

    private val api = apiFactory.create(WidgetApi::class.java)

    override suspend fun getLastPhoto(): PhotoWidgetResponse {
        return api.getLastPhoto()
    }
}
