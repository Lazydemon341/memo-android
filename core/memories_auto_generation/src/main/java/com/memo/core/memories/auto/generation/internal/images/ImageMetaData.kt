package com.memo.core.memories.auto.generation.internal.images

import android.net.Uri
import com.memo.core.model.Location

internal data class ImageMetaData(
    val uri: Uri,
    val dateTimeMillis: Long?,
    val location: Location?,
) {
    override fun toString() = super.toString()
}
