package com.memo.memories.internal.data.network.model

import com.memo.core.network.model.common.LocationDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class MemoryPhotoDTO(
    @Json(name = "photo_url")
    val photoUrl: String,

    @Json(name = "location")
    val location: LocationDTO?,

    @Json(name = "caption")
    val caption: String?,
)
