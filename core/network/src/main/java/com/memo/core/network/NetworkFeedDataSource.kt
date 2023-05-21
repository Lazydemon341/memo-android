package com.memo.core.network

import com.memo.core.network.model.feed.FeedRequest
import com.memo.core.network.model.feed.FeedResponse

interface NetworkFeedDataSource {

    suspend fun getPosts(data: FeedRequest): FeedResponse
}
