package com.memo.core.memories.auto.generation.internal

import com.memo.core.memories.auto.generation.api.MemoriesGenerator
import com.memo.core.memories.auto.generation.api.models.MemoryRaw
import com.memo.core.memories.auto.generation.api.models.MemoryRequestParams
import com.memo.core.memories.auto.generation.internal.description.MemoryDescription
import com.memo.core.memories.auto.generation.internal.description.MemoryDescriptionRepository
import com.memo.core.memories.auto.generation.internal.images.ImageMetaData
import com.memo.core.memories.auto.generation.internal.images.ImagesRepository
import com.memo.core.memories.auto.generation.internal.images.ImagesRequestParams
import com.memo.core.memories.auto.generation.internal.ranges.MemoriesRangesGenerator
import com.memo.core.memories.auto.generation.internal.ranges.MemoryRange
import com.memo.core.memories.auto.generation.internal.scoring.MemoriesScoringRepository
import com.memo.core.memories.auto.generation.internal.scoring.MemoryAfterScoring
import com.memo.core.memories.auto.generation.internal.tags.MemoryWithTags
import com.memo.core.memories.auto.generation.internal.tags.TagsRepository
import com.memo.core.model.memories.MemoryPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MemoriesGeneratorImpl @Inject constructor(
    private val imagesRepository: ImagesRepository,
    private val memoriesRangesGenerator: MemoriesRangesGenerator,
    private val scoringRepository: MemoriesScoringRepository,
    private val tagsRepository: TagsRepository,
    private val memoryDescriptionRepository: MemoryDescriptionRepository
) : MemoriesGenerator {

    override suspend fun generateMemories(params: MemoryRequestParams): List<MemoryRaw> = withContext(Dispatchers.Default) {
        val images = imagesRepository.getImagesMetaData(
            getImagesRequestParams(params)
        )
        val ranges = memoriesRangesGenerator.concatImagesIntoMemoryRanges(images, params)
        val memoriesAfterScoring = processMemoriesIntoScoringModel(ranges, params)
        val memoriesWithTags = processMemoriesIntoTagsModel(memoriesAfterScoring)
        return@withContext memoriesWithTags.map {
            val description = getMemoryDescription(it)
            mapToExternalModel(it, description)
        }
    }

    private fun getImagesRequestParams(params: MemoryRequestParams): ImagesRequestParams {
        return ImagesRequestParams(
            startTimestamp = params.startDateTimestamp,
            endTimestamp = params.endDateTimestamp
        )
    }

    private suspend fun processMemoriesIntoScoringModel(ranges: List<MemoryRange>, params: MemoryRequestParams): List<MemoryAfterScoring> {
        return supervisorScope {
            ranges.map { range ->
                async {
                    scoringRepository.processMemoryRange(range, params)
                }
            }.awaitAll()
        }
    }

    private suspend fun processMemoriesIntoTagsModel(memories: List<MemoryAfterScoring>): List<MemoryWithTags> {
        return supervisorScope {
            memories.map {
                withContext(Dispatchers.Default) {
                    tagsRepository.processMemory(it)
                }
            }
        }
    }

    private suspend fun getMemoryDescription(memoryWithTags: MemoryWithTags): MemoryDescription {
        return memoryDescriptionRepository.getDescriptionAndTitleForPhotos(memoryWithTags.images.map { it.image })
    }

    private fun mapToExternalModel(memory: MemoryWithTags, memoryDescription: MemoryDescription): MemoryRaw {
        return MemoryRaw(
            title = memoryDescription.title,
            caption = memoryDescription.caption,
            photos = memory.images.map {
                convertToMemoryPhoto(it.image, it.tags)
            },
        )
    }

    private fun convertToMemoryPhoto(imageMetaData: ImageMetaData, tags: List<String>): MemoryPhoto {
        return MemoryPhoto(
            photoUri = imageMetaData.uri.toString(),
            timestamp = imageMetaData.dateTimeMillis,
            photoLocation = imageMetaData.location,
            tags = tags,
        )
    }
}
