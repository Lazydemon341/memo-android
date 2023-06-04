package com.memo.camera.internal.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.memo.features.camera.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun CameraPermission() {
    var permissionRequested by rememberSaveable {
        mutableStateOf(false)
    }
    if (permissionRequested) {
        return
    }

    val permissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    if (permissionState.status.isGranted) {
        return
    }

    val textToShow = if (permissionState.status.shouldShowRationale) {
        R.string.camera_permission_rationale
    } else {
        R.string.camera_permission_description
    }
    PermissionDialog(text = stringResource(id = textToShow)) {
        permissionState.launchPermissionRequest()
    }
    permissionRequested = true
}

@Composable
private fun PermissionDialog(
    text: String,
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text(text = "Permission request")
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("Ok")
            }
        }
    )
}
