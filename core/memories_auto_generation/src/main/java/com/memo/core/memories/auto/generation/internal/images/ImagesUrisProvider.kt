package com.memo.core.memories.auto.generation.internal.images

import android.content.Context
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.MediaColumns
import androidx.annotation.ChecksSdkIntAtLeast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

internal class ImagesUrisProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    @ChecksSdkIntAtLeast(api = VERSION_CODES.Q)
    private val isAtLeastQ = VERSION.SDK_INT >= VERSION_CODES.Q

    private val collection = if (VERSION.SDK_INT >= VERSION_CODES.Q) {
        Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )
    } else {
        Media.EXTERNAL_CONTENT_URI
    }

    private val targetColumns = arrayOf(
        Media._ID,
    )

    @Throws(FileNotFoundException::class, IOException::class)
    suspend fun getAllImagesUrisSortedByDateTaken(): List<Uri> = withContext(Dispatchers.IO) {
        val result = mutableListOf<Uri>()
        // TODO поддержать выбор по времени

        context.contentResolver.query(
            collection, targetColumns, null, null, SORT_ORDER
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaColumns._ID)

            while (cursor.moveToNext()) {

                val photoId = cursor.getString(idColumn)

                var photoUri: Uri = Uri.withAppendedPath(
                    Media.EXTERNAL_CONTENT_URI, photoId
                )
                if (isAtLeastQ) {
                    photoUri = MediaStore.setRequireOriginal(photoUri)
                }
                result.add(photoUri)
            }
        }
        return@withContext result
    }

    private companion object {
        private const val SORT_ORDER = "${Media.DATE_TAKEN} DESC"
    }
}
