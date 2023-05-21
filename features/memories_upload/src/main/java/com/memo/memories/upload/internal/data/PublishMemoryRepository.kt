package com.memo.memories.upload.internal.data

import android.graphics.Bitmap
import android.net.Uri
import com.memo.core.data.mappers.toLocationDTO
import com.memo.core.data.photos.ImagesProvider
import com.memo.core.data.repository.memories.GeneratedMemoriesRepository
import com.memo.core.network.NetworkMemoryDataSource
import com.memo.core.network.NetworkPhotoDataSource
import com.memo.core.network.model.memory.MemoryCreateRequestBody
import com.memo.core.network.model.memory.MemoryPhotoDto
import com.memo.core.storage.SessionBitmapFileStorage
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import com.memo.core.utils.trySuspend
import javax.inject.Inject

internal class PublishMemoryRepository @Inject constructor(
    private val memoriesRepository: GeneratedMemoriesRepository,
    private val networkPhotoDataSource: NetworkPhotoDataSource,
    private val networkMemoryDataSource: NetworkMemoryDataSource,
    private val sessionBitmapFileStorage: SessionBitmapFileStorage,
    private val imagesProvider: ImagesProvider,
) {

    suspend fun publishMemory(memoryId: Long): Result<Long> {
        return suspendRunCatching {
            val memory = memoriesRepository.getMemoryById(memoryId)
                ?: return Result.failure(IllegalArgumentException("No memory found"))
            val uris = memory.photos.map {
                Uri.parse(it.photoUri)
            }

            val photoIds = uris.map {
                loadImageOnServer(it)
            }

            val memoryDto = MemoryCreateRequestBody(
                name = memory.title,
                caption = memory.caption,
                photos = photoIds.mapIndexed { index, id ->
                    MemoryPhotoDto(
                        photoId = id,
                        location = memory.photos.getOrNull(index)?.photoLocation?.toLocationDTO(),
                        caption = memory.photos.getOrNull(index)?.tags?.joinToString(separator = " ") { "#$it" }
                    )
                }
            )

            retryOnNetworkOrServerErrors {
                networkMemoryDataSource.uploadMemory(memoryDto)
            }.memoryId
        }
    }

    private suspend fun loadImageOnServer(uri: Uri): Long {
        return trySuspend(
            run = {
                loadImageIntoFileStorage(uri)
                val file = sessionBitmapFileStorage.acquireFile()
                retryOnNetworkOrServerErrors {
                    networkPhotoDataSource.uploadPhoto(file)
                }.photosIds.firstOrNull() ?: throw IllegalStateException("No photo id")
            },
            catch = {
                throw it
            },
            finally = { sessionBitmapFileStorage.releaseFile() }
        )
    }

    private suspend fun loadImageIntoFileStorage(imageUri: Uri) {
        val bitmap = imagesProvider.getImageByUri(imageUri)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, IMAGE_TARGET_SIZE, IMAGE_TARGET_SIZE, false)
        sessionBitmapFileStorage.writeToFile(resizedBitmap)
    }

    private companion object {
        private const val IMAGE_TARGET_SIZE = 1024
    }
}
