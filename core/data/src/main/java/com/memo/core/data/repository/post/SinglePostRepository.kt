package com.memo.core.data.repository.post

import com.memo.core.model.feed.Post

interface SinglePostRepository {

    suspend fun getPostById(postId: Long): Result<Post>
}
