package com.memo.core.network

import com.memo.core.network.model.post.PostDTO
import com.memo.core.network.model.post.PostUploadParams

interface NetworkSinglePostDataSource {

    suspend fun upload(postUploadParams: PostUploadParams)

    suspend fun getPostById(postId: Long): PostDTO
}
