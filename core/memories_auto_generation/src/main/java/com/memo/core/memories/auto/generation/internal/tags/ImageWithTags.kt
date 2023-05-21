package com.memo.core.memories.auto.generation.internal.tags

import com.memo.core.memories.auto.generation.internal.images.ImageMetaData

internal data class ImageWithTags(
    val image: ImageMetaData,
    val tags: List<String>
)
