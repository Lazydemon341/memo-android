package com.memo.camera.internal.ui.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.otaliastudios.cameraview.CameraView

@Composable
internal fun CameraPreviewWithFlashModeButton(
    cameraView: CameraView,
    hasFlashUnitProvider: () -> Boolean,
    flashModeEnabledProvider: () -> Boolean,
    onFlashModeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        AndroidView(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(48.dp)),
            factory = { cameraView },
        )
        FlashModeButton(
            modifier = Modifier.padding(16.dp),
            visibleProvider = hasFlashUnitProvider,
            enabledProvider = flashModeEnabledProvider,
            onClick = onFlashModeClick,
        )
    }
}

@Composable
private fun FlashModeButton(
    visibleProvider: () -> Boolean,
    enabledProvider: () -> Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(visible = visibleProvider()) {
        IconButton(
            modifier = modifier,
            onClick = onClick,
        ) {
            Crossfade(targetState = enabledProvider()) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = when (it) {
                        true -> Filled.FlashlightOn
                        false -> Filled.FlashlightOff
                    },
                    contentDescription = "",
                    tint = Color.White,
                )
            }
        }
    }
}
