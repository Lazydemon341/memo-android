package com.memo.core.memories.auto.generation.internal.images

import android.net.Uri
import com.memo.core.memories.auto.generation.internal.exif.ExifMetaInfProvider
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

internal class ImagesMetaInfProvider @Inject constructor(
    private val exifMetaInfProvider: ExifMetaInfProvider,
) {

    @Throws(FileNotFoundException::class, IOException::class)
    suspend fun getSelectedImagesMetaData(uris: List<Uri>): List<ImageMetaData?> {
        return uris.map { exifMetaInfProvider.getImageMetaData(it) }
    }
}
