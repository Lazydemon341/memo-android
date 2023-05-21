package com.memo.memories.generation.internal.usecase

import com.memo.core.data.repository.memories.GeneratedMemoriesRepository
import com.memo.core.memories.auto.generation.api.MemoriesGenerator
import com.memo.core.memories.auto.generation.api.models.MemoryRequestParams
import com.memo.core.model.memories.GeneratedMemory
import javax.inject.Inject
import kotlin.random.Random

class GenerateNewMemoriesUseCase @Inject constructor(
    private val generatedMemoriesRepository: GeneratedMemoriesRepository,
    private val memoriesGenerator: MemoriesGenerator
) {

    suspend fun generate(params: MemoryRequestParams): Int {
        val generatedMemories = memoriesGenerator.generateMemories(params).map {
            GeneratedMemory(
                memoryId = Random.nextLong(),
                title = it.title,
                caption = it.caption,
                photos = it.photos,
            )
        }
        generatedMemoriesRepository.clear()
        generatedMemoriesRepository.addMemories(generatedMemories)
        return generatedMemories.size
    }
}
