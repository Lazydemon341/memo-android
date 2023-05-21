package com.memo.feed.internal.ui.model

import com.memo.core.model.feed.Post

data class PostUiState(
    val postId: Long,
    val caption: String?,
    val postImageUrl: String,
    val profileId: Long,
    val profileImageUrl: String?,
    val profileName: String,
    val publicationTimeInMillis: Long,
) {
    val shouldShowCaption = caption.isNullOrBlank().not()
}

fun Post.toPostUiState(): PostUiState {
    return PostUiState(
        postId = postId,
        caption = caption,
        postImageUrl = postImageUrl,
        profileId = profileId,
        profileImageUrl = profileImageUrl,
        profileName = profileName,
        publicationTimeInMillis = publicationTimestamp * 1000,
    )
}
