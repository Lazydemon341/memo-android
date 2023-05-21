package com.memo.core.network.model.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PeriodDTO(
    @Json(name = "start")
    val start: Long,
    @Json(name = "end")
    val end: Long,
)
