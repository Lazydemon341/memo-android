package com.memo.core.model.memories

import com.memo.core.model.Location

data class MemoryPhoto(
    val photoUri: String,
    val timestamp: Long?,
    val photoLocation: Location?,
    val tags: List<String>,
)
