package com.memo.core.memories.auto.generation.internal.exif

import android.content.Context
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.memo.core.memories.auto.generation.internal.images.ImageMetaData
import com.memo.core.model.Location
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

internal class ExifMetaInfProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    @Throws(FileNotFoundException::class, IOException::class)
    suspend fun getImageMetaData(imageUri: Uri): ImageMetaData? {
        return withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(imageUri)?.use { stream ->
                val exifInterface = ExifInterface(stream)
                val latLonArray = exifInterface.latLong
                val date: Long? = exifInterface.getImageDateTime()
                ImageMetaData(
                    uri = imageUri,
                    dateTimeMillis = date,
                    location = latLonArray?.let { latLonArray ->
                        Location(
                            latitude = latLonArray[0].toFloat(),
                            longitude = latLonArray[1].toFloat(),
                        )
                    }
                )
            }
        }
    }
}
