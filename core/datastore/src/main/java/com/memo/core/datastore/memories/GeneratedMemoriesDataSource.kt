package com.memo.core.datastore.memories

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class GeneratedMemoriesDataSource @Inject constructor(
    private val dataStore: DataStore<Memories>,
) {

    val generatedMemoriesFlow = dataStore.data

    val isEmpty = dataStore.data.map { it.memories.isEmpty() }

    @Throws(IOException::class)
    suspend fun addMemories(memoriesList: List<Memory>) {
        dataStore.updateData {
            val oldMemoriesList = it.memories
            val mergedList = oldMemoriesList.toMutableList()
            mergedList.addAll(memoriesList)
            it.copy(memories = mergedList)
        }
    }

    @Throws(IOException::class)
    suspend fun addMemory(memory: Memory) {
        dataStore.updateData {
            val oldMemoriesList = it.memories
            val mergedList = oldMemoriesList.toMutableList()
            mergedList.add(memory)
            it.copy(memories = mergedList)
        }
    }

    @Throws(IOException::class)
    suspend fun clear() {
        dataStore.updateData {
            it.copy(emptyList())
        }
    }

    suspend fun getMemoryById(memoryId: Long): Memory? {
        return generatedMemoriesFlow.first().memories.firstOrNull {
            it.local_id == memoryId
        }
    }
}
