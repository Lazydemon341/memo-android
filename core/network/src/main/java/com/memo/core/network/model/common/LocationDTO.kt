package com.memo.core.network.model.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDTO(
    @Json(name = "latitude")
    val latitude: Float,
    @Json(name = "longitude")
    val longitude: Float,
)
