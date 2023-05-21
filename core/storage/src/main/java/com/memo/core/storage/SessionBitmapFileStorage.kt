package com.memo.core.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

private const val BITMAP_COMPRESSION_QUALITY = 100

private const val FILE_PREFIX = "bitmap"
private const val FILE_SUFFIX = "storage"

@Singleton
class SessionBitmapFileStorage @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    private val file by lazy { createFile() }

    // Use ReadWrite mutex, when available:
    // https://github.com/Kotlin/kotlinx.coroutines/issues/94
    // Or use another synchronization technique
    private val mutex = Mutex()

    suspend fun writeToFile(bitmap: Bitmap) = withContext(Dispatchers.IO) {
        val byteArray = bitmap.toByteArray()
        mutex.withLock {
            FileOutputStream(file).use { stream ->
                stream.write(byteArray)
            }
        }
    }

    suspend fun readAsBitmap(): Bitmap = withContext(Dispatchers.IO) {
        mutex.withLock {
            BitmapFactory.decodeFile(file.absolutePath)
        }
    }

    suspend fun acquireFile(): File {
        mutex.lock()
        return file
    }

    fun releaseFile() {
        mutex.unlock()
    }

    private fun createFile(): File {
        return File.createTempFile(FILE_PREFIX, FILE_SUFFIX, context.cacheDir)
            .apply { deleteOnExit() }
    }

    private fun Bitmap.toByteArray(): ByteArray {
        return ByteArrayOutputStream().use { stream ->
            this.compress(Bitmap.CompressFormat.JPEG, BITMAP_COMPRESSION_QUALITY, stream)
            stream.toByteArray()
        }
    }
}
