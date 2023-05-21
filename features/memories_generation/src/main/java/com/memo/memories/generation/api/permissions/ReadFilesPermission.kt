package com.memo.memories.generation.api.permissions

import android.Manifest
import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
internal fun CheckForImagesReadPermission(
    onPermissionsGranted: () -> Unit,
) {

    val context = LocalContext.current
    var isAccessMediaLocationPermissionGranted by rememberSaveable {
        mutableStateOf(
            android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_MEDIA_LOCATION
            ) != PackageManager.PERMISSION_DENIED
        )
    }
    var isReadMediaImagesPermissionGranted by rememberSaveable {
        mutableStateOf(
            android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_DENIED
        )
    }
    var isReadExternalStoragePermissionGranted by rememberSaveable {
        mutableStateOf(
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_DENIED
        )
    }

    checkAndLaunchPermissionRequest(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE,
        isPermissionRequestRequired = {
            android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S_V2 &&
                !isReadExternalStoragePermissionGranted
        }
    ) {
        isReadExternalStoragePermissionGranted = true
    }

    if (isReadExternalStoragePermissionGranted) {
        checkAndLaunchPermissionRequest(
            permission = permission.ACCESS_MEDIA_LOCATION,
            isPermissionRequestRequired = {
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q &&
                    !isAccessMediaLocationPermissionGranted
            }
        ) {
            isAccessMediaLocationPermissionGranted = true
        }
    }

    if (isAccessMediaLocationPermissionGranted) {
        checkAndLaunchPermissionRequest(
            permission = permission.READ_MEDIA_IMAGES,
            isPermissionRequestRequired = {
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU && !isReadMediaImagesPermissionGranted
            }
        ) {
            isReadMediaImagesPermissionGranted = true
        }
    }

    if ((isAccessMediaLocationPermissionGranted || isReadExternalStoragePermissionGranted) && isReadMediaImagesPermissionGranted) {
        onPermissionsGranted()
    }
}

@Composable
private fun checkAndLaunchPermissionRequest(
    permission: String,
    isPermissionRequestRequired: () -> Boolean,
    onPermissionGranted: () -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        }
    }
    if (
        isPermissionRequestRequired()
    ) {
        LaunchedEffect(Unit) {
            launcher.launch(
                permission
            )
        }
    } else {
        onPermissionGranted()
    }
}

internal fun isStoragePermissionsGranted(
    context: Context
): Boolean {
    val mediaLocationPermissionGranted =
        android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_MEDIA_LOCATION
        ) != PackageManager.PERMISSION_DENIED

    val readMediaPermissionGranted =
        android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_MEDIA_IMAGES
        ) != PackageManager.PERMISSION_DENIED

    val externalStoragePermissionGranted =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S_V2 ||
            ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_DENIED

    return (mediaLocationPermissionGranted || readMediaPermissionGranted) && externalStoragePermissionGranted
}
