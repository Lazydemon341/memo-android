package com.memo.core.model.feed

import com.memo.core.model.Location

data class Post(
    val postId: Long,
    val caption: String?,
    val postImageUrl: String,
    val profileId: Long,
    val profileImageUrl: String?,
    val profileName: String,
    val publicationTimestamp: Long,
    val location: Location?
)
