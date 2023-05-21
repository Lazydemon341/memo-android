package com.memo.core.memories.auto.generation.internal.ranges

import com.memo.core.memories.auto.generation.internal.images.ImageMetaData

internal class MemoryRange(
    val images: List<ImageMetaData>,
) {
    val imagesCount = images.size
}
