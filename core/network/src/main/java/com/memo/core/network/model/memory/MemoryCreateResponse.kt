package com.memo.core.network.model.memory

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MemoryCreateResponse(
    @Json(name = "memory_id")
    val memoryId: Long,
)
