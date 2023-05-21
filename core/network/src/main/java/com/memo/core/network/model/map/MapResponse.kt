package com.memo.core.network.model.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MapResponse(
    @Json(name = "annotations")
    val annotations: List<PhotoOnMapDTO>
)
