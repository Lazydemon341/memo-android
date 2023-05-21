package com.memo.core.memories.auto.generation.internal.description

import android.content.Context
import android.text.format.DateUtils
import com.memo.core.memories.auto.generation.R
import com.memo.core.memories.auto.generation.internal.images.ImageMetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class MemoryCaptionProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getCaptionForPhotos(photos: List<ImageMetaData>): String {
        val (start, end) = getImagesMinAndMaxTimestamps(photos)
        if (start == null || end == null) {
            return context.getString(R.string.memory_default_caption)
        }
        return formatDateString(start, end)
    }

    private fun getImagesMinAndMaxTimestamps(images: List<ImageMetaData>): Pair<Long?, Long?> {
        var minTimestamp = Long.MAX_VALUE
        var maxTimestamp = Long.MIN_VALUE
        images.forEach { imageMetaData ->
            imageMetaData.dateTimeMillis?.let { timestamp ->
                if (timestamp < minTimestamp) {
                    minTimestamp = timestamp
                }
                if (timestamp > maxTimestamp) {
                    maxTimestamp = timestamp
                }
            }
        }
        return minTimestamp to maxTimestamp
    }

    private fun formatDateString(startDate: Long, endDate: Long): String {
        return DateUtils.formatDateRange(
            context, startDate, endDate,
            DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_ABBREV_MONTH
        )
    }
}
