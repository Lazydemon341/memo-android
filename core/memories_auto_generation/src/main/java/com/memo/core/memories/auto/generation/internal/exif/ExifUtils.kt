package com.memo.core.memories.auto.generation.internal.exif

import android.annotation.SuppressLint
import androidx.exifinterface.media.ExifInterface

@SuppressLint("RestrictedApi")
internal fun ExifInterface.getImageDateTime(): Long? {
    return try {
        this.gpsDateTime ?: this.dateTime
    } catch (e: Throwable) {
        null
    }
}
