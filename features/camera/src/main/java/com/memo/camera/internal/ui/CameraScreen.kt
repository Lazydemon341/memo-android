package com.memo.camera.internal.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.memo.camera.internal.takePictureOrSnapshot
import com.memo.camera.internal.toggleFlash
import com.memo.camera.internal.ui.base.CameraActions
import com.memo.camera.internal.ui.base.CameraPreviewWithFlashModeButton
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Flash.ON

@Composable
internal fun CameraScreen(
    onPhotoTaken: (Bitmap) -> Unit,
) {
    CameraScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        onPhotoTaken = onPhotoTaken,
    )
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
private fun CameraScreenContent(
    onPhotoTaken: (Bitmap) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var cameraFacing by rememberSaveable { mutableStateOf(Facing.FRONT) }
    var flashMode by rememberSaveable { mutableStateOf(Flash.OFF) }
    val hasFlashUnit = remember { mutableStateOf(false) }
    val cameraView = remember {
        createCameraView(
            context = context,
            cameraFacing = cameraFacing,
            flashMode = flashMode,
            onPhotoTaken = onPhotoTaken,
            onCameraOpened = {
                hasFlashUnit.value = it.supportedFlash.contains(Flash.ON)
            }
        )
    }
    RegisterCameraLifecycle(cameraView = cameraView)
    ConstraintLayout(
        modifier = modifier
    ) {
        val (camera, actions) = createRefs()
        CameraPreviewWithFlashModeButton(
            modifier = Modifier.constrainAs(camera) {
                top.linkTo(parent.top, margin = 64.dp)
            },
            cameraView = cameraView,
            hasFlashUnitProvider = hasFlashUnit::value,
            flashModeEnabledProvider = { flashMode == ON },
            onFlashModeClick = {
                flashMode = cameraView.toggleFlash()
            },
        )
        CameraActions(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(actions) {
                    top.linkTo(camera.bottom)
                    bottom.linkTo(parent.bottom)
                },
            onImagePicked = onPhotoTaken,
            onTakePicture = cameraView::takePictureOrSnapshot,
            onSwapCamera = {
                cameraFacing = cameraView.toggleFacing()
            },
        )
    }
}

@Composable
private fun RegisterCameraLifecycle(
    cameraView: CameraView,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> cameraView.open()
                Lifecycle.Event.ON_PAUSE -> cameraView.close()
                Lifecycle.Event.ON_DESTROY -> cameraView.destroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            cameraView.destroy()
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun CameraCapturePreview() {
    CameraScreenContent({})
}
