package com.memo.core.data.repository.post

import com.memo.core.data.mappers.toPost
import com.memo.core.model.feed.Post
import com.memo.core.network.NetworkSinglePostDataSource
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SinglePostRepositoryImpl @Inject constructor(
    private val network: NetworkSinglePostDataSource,
) : SinglePostRepository {

    override suspend fun getPostById(postId: Long): Result<Post> = withContext(Dispatchers.IO) {
        suspendRunCatching {
            retryOnNetworkOrServerErrors {
                network.getPostById(postId).toPost()
            }
        }
    }
}
