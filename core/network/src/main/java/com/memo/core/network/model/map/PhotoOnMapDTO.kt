package com.memo.core.network.model.map

import com.memo.core.network.model.common.LocationDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoOnMapDTO(
    @Json(name = "location")
    val locationDTO: LocationDTO,
    @Json(name = "photo_url")
    val photoUrl: String,
    @Json(name = "post_id")
    val postId: Long,
    @Json(name = "user_id")
    val userId: Long,
)
