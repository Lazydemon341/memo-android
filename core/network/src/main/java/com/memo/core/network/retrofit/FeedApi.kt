package com.memo.core.network.retrofit

import com.memo.core.network.NetworkFeedDataSource
import com.memo.core.network.model.feed.FeedRequest
import com.memo.core.network.model.feed.FeedResponse
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface FeedApi {

    @POST("feed")
    suspend fun getPosts(@Body data: FeedRequest): FeedResponse
}

@Singleton
class RetrofitFeedDataSource @Inject constructor(
    apiFactory: ApiFactory,
) : NetworkFeedDataSource {

    private val api = apiFactory.create(FeedApi::class.java)

    override suspend fun getPosts(data: FeedRequest): FeedResponse {
        return api.getPosts(data)
    }
}
