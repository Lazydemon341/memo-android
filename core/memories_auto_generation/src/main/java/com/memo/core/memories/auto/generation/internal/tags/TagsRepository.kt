package com.memo.core.memories.auto.generation.internal.tags

import com.memo.core.memories.auto.generation.internal.images.ImageMetaData
import com.memo.core.memories.auto.generation.internal.scoring.MemoryAfterScoring
import javax.inject.Inject

internal class TagsRepository @Inject constructor(
    private val modelAdapter: ImagesTagsModelAdapter,
) {

    fun processMemory(memory: MemoryAfterScoring): MemoryWithTags {
        return MemoryWithTags(
            images = memory.sortedImages.map { getTagsForImage(it) },
        )
    }

    private fun getTagsForImage(image: ImageMetaData): ImageWithTags {
        val tags = try {
            modelAdapter.classify(image.uri)
        } catch (e: Throwable) {
            emptyList()
        }
        return ImageWithTags(image, tags)
    }
}
