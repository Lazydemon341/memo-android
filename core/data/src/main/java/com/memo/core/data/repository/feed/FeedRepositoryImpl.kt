package com.memo.core.data.repository.feed

import com.memo.core.data.mappers.toPost
import com.memo.core.model.feed.Post
import com.memo.core.network.NetworkFeedDataSource
import com.memo.core.network.model.feed.FeedRequest
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkFeedDataSource,
) : FeedRepository {

    override suspend fun getPosts(fromId: Long?, size: Int): Result<List<Post>> {
        return suspendRunCatching {
            val requestData = FeedRequest(amount = size, oldestPostId = fromId)
            retryOnNetworkOrServerErrors {
                networkDataSource.getPosts(requestData)
            }.posts.map { it.toPost() }
        }
    }
}
