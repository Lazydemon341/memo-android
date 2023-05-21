package com.memo.core.memories.auto.generation.internal.description

import com.memo.core.memories.auto.generation.internal.description.title.MemoryTitleProvider
import com.memo.core.memories.auto.generation.internal.images.ImageMetaData
import javax.inject.Inject

internal class MemoryDescriptionRepository @Inject constructor(
    private val titleProvider: MemoryTitleProvider,
    private val captionProvider: MemoryCaptionProvider,
) {

    suspend fun getDescriptionAndTitleForPhotos(photos: List<ImageMetaData>): MemoryDescription {
        return MemoryDescription(
            title = titleProvider.getTitleForPhotos(photos),
            caption = captionProvider.getCaptionForPhotos(photos),
        )
    }
}
