package com.memo.core.data.mappers

import com.memo.core.model.feed.Post
import com.memo.core.network.model.post.PostDTO

internal fun PostDTO.toPost(): Post {
    return Post(
        postId = postId,
        caption = caption,
        postImageUrl = photoUrl,
        profileImageUrl = user.photoUrl,
        profileId = user.id,
        profileName = "${user.name} ${user.surname}",
        publicationTimestamp = publicationTime,
        location = location?.toLocation()
    )
}
