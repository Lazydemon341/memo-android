package com.memo.core.model.map

import com.memo.core.model.Location

data class PhotoOnMap(
    val location: Location,
    val photoUrl: String,
    val postId: Long,
    val userId: Long,
)
