package com.memo.core.network.model.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MapRequest(
    @Json(name = "period")
    val period: PeriodDTO?,
)
