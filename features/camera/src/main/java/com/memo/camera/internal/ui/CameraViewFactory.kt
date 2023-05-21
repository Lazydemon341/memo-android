package com.memo.camera.internal.ui

import android.content.Context
import android.graphics.Bitmap
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Audio.OFF
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode.PICTURE
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction

internal fun createCameraView(
    context: Context,
    cameraFacing: Facing,
    flashMode: Flash,
    onPhotoTaken: (Bitmap) -> Unit,
    onCameraOpened: (CameraOptions) -> Unit,
): CameraView {
    return CameraView(context).apply {
        setRequestPermissions(false)
        setExperimental(true)
        engine = Engine.CAMERA2
        audio = OFF
        mode = PICTURE
        facing = cameraFacing
        flash = flashMode
        playSounds = false
        mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
        addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap { bitmap ->
                    bitmap?.let { onPhotoTaken(it) }
                }
            }

            override fun onCameraOpened(options: CameraOptions) {
                onCameraOpened(options)
            }
        })
    }
}
