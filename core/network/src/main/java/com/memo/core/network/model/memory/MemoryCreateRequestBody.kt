package com.memo.core.network.model.memory

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MemoryCreateRequestBody(
    @Json(name = "name")
    val name: String,
    @Json(name = "caption")
    val caption: String,
    @Json(name = "photos")
    val photos: List<MemoryPhotoDto>
)
