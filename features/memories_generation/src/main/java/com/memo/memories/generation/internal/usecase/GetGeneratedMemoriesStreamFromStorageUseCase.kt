package com.memo.memories.generation.internal.usecase

import com.memo.core.data.repository.memories.GeneratedMemoriesRepository
import javax.inject.Inject

class GetGeneratedMemoriesStreamFromStorageUseCase @Inject constructor(
    generatedMemoriesRepository: GeneratedMemoriesRepository,
) {

    val memoriesFlow = generatedMemoriesRepository.generatedMemories
}
