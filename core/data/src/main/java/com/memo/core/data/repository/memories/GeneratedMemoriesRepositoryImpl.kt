package com.memo.core.data.repository.memories

import com.memo.core.datastore.memories.GeneratedMemoriesDataSource
import com.memo.core.datastore.memories.toExternalModel
import com.memo.core.datastore.memories.toPersistableModel
import com.memo.core.model.memories.GeneratedMemory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GeneratedMemoriesRepositoryImpl @Inject constructor(
    private val local: GeneratedMemoriesDataSource
) : GeneratedMemoriesRepository {

    override val generatedMemories: Flow<List<GeneratedMemory>> =
        local.generatedMemoriesFlow.map { memoriesWrapper ->
            memoriesWrapper.memories.map { it.toExternalModel() }
        }

    override suspend fun clear() {
        local.clear()
    }

    override suspend fun addMemories(list: List<GeneratedMemory>) {
        local.addMemories(
            list.map { it.toPersistableModel() }
        )
    }

    override suspend fun addMemory(memory: GeneratedMemory) {
        local.addMemory(
            memory.toPersistableModel()
        )
    }

    override suspend fun getMemoryById(memoryId: Long): GeneratedMemory? {
        return local.getMemoryById(memoryId)?.toExternalModel()
    }
}
