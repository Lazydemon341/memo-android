package com.memo.memories.internal.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class MemoryResponse(
    @Json(name = "name")
    val name: String,

    @Json(name = "caption")
    val caption: String,

    @Json(name = "creator")
    val creator: MemoryCreatorDTO,

    @Json(name = "photos")
    val photos: List<MemoryPhotoDTO>,
)
