package com.memo.core.data.repository.memories

import com.memo.core.model.memories.GeneratedMemory
import kotlinx.coroutines.flow.Flow

interface GeneratedMemoriesRepository {

    val generatedMemories: Flow<List<GeneratedMemory>>

    suspend fun clear()

    suspend fun addMemories(list: List<GeneratedMemory>)

    suspend fun addMemory(memory: GeneratedMemory)

    suspend fun getMemoryById(memoryId: Long): GeneratedMemory?
}
