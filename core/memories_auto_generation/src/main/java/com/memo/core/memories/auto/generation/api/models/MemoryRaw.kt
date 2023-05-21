package com.memo.core.memories.auto.generation.api.models

import com.memo.core.model.memories.MemoryPhoto

data class MemoryRaw(
    val title: String,
    val caption: String,
    val photos: List<MemoryPhoto>,
) {
    override fun toString() = super.toString()
}
