package com.memo.core.memories.auto.generation.internal.images

import android.net.Uri
import javax.inject.Inject

internal class ImagesRepository @Inject constructor(
    private val imagesMetaInfProvider: ImagesMetaInfProvider,
    private val imagesUrisProvider: ImagesUrisProvider,
) {

    suspend fun getImagesMetaData(params: ImagesRequestParams): List<ImageMetaData> {
        val uris = if (params == null) {
            imagesUrisProvider.getAllImagesUrisSortedByDateTaken()
        } else {
            imagesUrisProvider.getAllImagesUrisSortedByDateTaken() // TODO по датам
        }
        return getImagesMetaData(uris)
    }

    private suspend fun getImagesMetaData(uris: List<Uri>): List<ImageMetaData> {
        return imagesMetaInfProvider.getSelectedImagesMetaData(uris).filterNotNull()
    }
}
