package com.memo.core.network.model.post

import com.memo.core.network.model.common.LocationDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PhotoDTO(
    @Json(name = "photo_id")
    val photoId: Long,

    @Json(name = "location")
    val location: LocationDTO?,
)
