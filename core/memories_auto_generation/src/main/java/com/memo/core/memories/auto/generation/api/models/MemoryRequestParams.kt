package com.memo.core.memories.auto.generation.api.models

data class MemoryRequestParams(
    val numberOfMemories: Int,
    val minPhotosInMemory: Int,
    val maxDaysBetweenMemorableDays: Int,
    val startDateTimestamp: Long? = null,
    val endDateTimestamp: Long? = null,
)
