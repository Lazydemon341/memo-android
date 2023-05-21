package com.memo.core.memories.auto.generation.internal.scoring

import com.memo.core.memories.auto.generation.api.models.MemoryRequestParams
import com.memo.core.memories.auto.generation.internal.ranges.MemoryRange
import javax.inject.Inject

internal class MemoriesScoringRepository @Inject constructor(
    private val scoringModelAdapter: ImagesScoringModelAdapter,
) {

    suspend fun processMemoryRange(range: MemoryRange, params: MemoryRequestParams): MemoryAfterScoring {
        val scores = scoringModelAdapter.getImagesScores(range.images)
        // TODO главная, preview
        return MemoryAfterScoring(
            sortedImages = range.images
        )
    }
}
