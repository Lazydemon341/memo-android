package com.memo.camera.internal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Flash

internal fun CameraView.takePictureOrSnapshot() {
    if (flash == Flash.OFF) {
        takePictureSnapshot()
    } else {
        takePicture()
    }
}

internal fun CameraView.toggleFlash(): Flash {
    flash = when (flash) {
        Flash.OFF -> Flash.ON
        else -> Flash.OFF
    }
    return flash
}

internal fun Uri.toBitmap(context: Context): Bitmap {
    return if (Build.VERSION.SDK_INT < VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    }
}
