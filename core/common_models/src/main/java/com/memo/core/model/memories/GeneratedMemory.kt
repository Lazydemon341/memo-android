package com.memo.core.model.memories

data class GeneratedMemory(
    val memoryId: Long,
    val title: String,
    val caption: String,
    val photos: List<MemoryPhoto>,
) {
    override fun toString() = super.toString()
}
