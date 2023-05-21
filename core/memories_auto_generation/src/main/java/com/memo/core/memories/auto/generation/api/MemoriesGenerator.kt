package com.memo.core.memories.auto.generation.api

import com.memo.core.memories.auto.generation.api.models.MemoryRaw
import com.memo.core.memories.auto.generation.api.models.MemoryRequestParams

interface MemoriesGenerator {
    suspend fun generateMemories(params: MemoryRequestParams): List<MemoryRaw>
}
