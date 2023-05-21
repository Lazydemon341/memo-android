package com.memo.core.network.model.post

import com.memo.core.network.model.common.LocationDTO
import com.memo.core.network.model.common.UserModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PostDTO(
    @Json(name = "user")
    val user: UserModel,
    @Json(name = "photo_url")
    val photoUrl: String,
    @Json(name = "publication_time")
    val publicationTime: Long,
    @Json(name = "caption")
    val caption: String?,
    @Json(name = "post_id")
    val postId: Long,
    @Json(name = "memory_id")
    val memoryId: Long?,
    @Json(name = "location")
    val location: LocationDTO?,
)
