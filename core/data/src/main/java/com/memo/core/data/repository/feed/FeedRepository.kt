package com.memo.core.data.repository.feed

import com.memo.core.model.feed.Post

interface FeedRepository {

    suspend fun getPosts(fromId: Long?, size: Int): Result<List<Post>>
}
