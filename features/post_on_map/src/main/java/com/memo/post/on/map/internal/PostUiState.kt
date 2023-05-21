package com.memo.post.on.map.internal

import com.memo.core.model.Location
import com.memo.core.model.feed.Post

internal data class PostUiState(
    val postId: Long,
    val caption: String?,
    val postImageUrl: String,
    val profileImageUrl: String?,
    val profileName: String,
    val publicationTimeInMillis: Long,
    val location: Location?
) {
    val shouldShowCaption = caption.isNullOrBlank().not()
}

internal fun Post.toPostUiState(): PostUiState {
    return PostUiState(
        postId = postId,
        caption = caption,
        postImageUrl = postImageUrl,
        profileImageUrl = profileImageUrl,
        profileName = profileName,
        publicationTimeInMillis = publicationTimestamp * 1000,
        location = location
    )
}
