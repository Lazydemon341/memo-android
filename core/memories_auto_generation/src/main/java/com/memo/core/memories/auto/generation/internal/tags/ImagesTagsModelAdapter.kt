package com.memo.core.memories.auto.generation.internal.tags

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.memo.core.data.photos.ImagesProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import javax.inject.Inject

internal class ImagesTagsModelAdapter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imagesProvider: ImagesProvider
) {

    private var imageClassifier: ObjectDetector? = null

    fun classify(imageUri: Uri): List<String> {
        if (imageClassifier == null) {
            setupImageClassifier()
        }
        return processImage(imageUri)
    }

    private fun processImage(imageUri: Uri): List<String> {

        val bitmap = if (VERSION.SDK_INT >= VERSION_CODES.P) {
            getImageByUri(imageUri)
        } else {
            return emptyList()
        }

        val imageProcessor = ImageProcessor.Builder()
            .build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
        val imageProcessingOptions = ImageProcessingOptions.builder()
            .build()

        val results = imageClassifier?.detect(tensorImage, imageProcessingOptions)
        return results?.let(::filterResults) ?: emptyList()
    }

    private fun filterResults(input: MutableList<Detection>): List<String> {
        return input.filter {
            if (it.categories.size == 0) return@filter false
            val targetCategory = it.categories[0]
            targetCategory.score >= MIN_CONFIDENCE
        }.map {
            it.categories[0]
        }.map {
            it.label
        }.distinct()
    }

    private fun setupImageClassifier() {

        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(THREADS_NUM)
        baseOptionsBuilder.useNnapi()

        val optionsBuilder = ObjectDetector.ObjectDetectorOptions.builder()
            .setScoreThreshold(MIN_CONFIDENCE)
            .setMaxResults(MAX_RESULTS)
            .setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier =
                ObjectDetector.createFromFileAndOptions(context, MODEL_FILENAME, optionsBuilder.build())
        } catch (_: IllegalStateException) {
        }
    }

    private fun getImageByUri(uri: Uri): Bitmap {
        val bitmap = imagesProvider.getImageByUri(uri)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, IMAGE_TARGET_SIZE, IMAGE_TARGET_SIZE, false)
        return resizedBitmap.copy(Bitmap.Config.ARGB_8888, false)
    }

    private companion object {
        private const val MIN_CONFIDENCE = 0.5f
        private const val MODEL_FILENAME = "tags_model.tflite"
        private const val MAX_RESULTS = 5
        private const val THREADS_NUM = 3
        private const val IMAGE_TARGET_SIZE = 320
    }
}
