package com.memo.camera.internal.ui.base

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memo.camera.internal.toBitmap
import com.memo.core.design.theme.MemoAppTheme

@Composable
internal fun CameraActions(
    onImagePicked: (Bitmap) -> Unit,
    onTakePicture: () -> Unit,
    onSwapCamera: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PickImageAction(onImagePicked = onImagePicked)
        Button(
            modifier = Modifier
                .size(64.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            content = {},
            onClick = onTakePicture,
        )
        IconButton(
            onClick = onSwapCamera,
            content = {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Filled.Sync,
                    contentDescription = "",
                    tint = Color.White,
                )
            },
        )
    }
}

@Composable
private fun PickImageAction(
    onImagePicked: (Bitmap) -> Unit,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = PickVisualMedia(),
        onResult = { uri ->
            uri?.toBitmap(context)?.let { onImagePicked(it) }
        },
    )
    val request = remember { PickVisualMediaRequest(ImageOnly) }
    IconButton(
        onClick = { launcher.launch(request) },
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Filled.Image,
            contentDescription = "",
            tint = Color.White,
        )
    }
}

@Composable
@Preview
private fun CameraScreenTopBarPreview() {
    MemoAppTheme {
        CameraActions(
            onImagePicked = {},
            onTakePicture = {},
            onSwapCamera = {},
        )
    }
}
