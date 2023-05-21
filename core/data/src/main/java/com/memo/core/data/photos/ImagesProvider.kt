package com.memo.core.data.photos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build.VERSION
import android.provider.MediaStore.Images.Media
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImagesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getImageByUri(uri: Uri): Bitmap {
        return when {
            VERSION.SDK_INT < 28 -> Media.getBitmap(
                context.contentResolver,
                uri
            )
            else -> {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }
}
