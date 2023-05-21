package com.memo.memories.generation.api

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.memo.memories.generation.api.permissions.CheckForImagesReadPermission
import com.memo.memories.generation.api.permissions.isStoragePermissionsGranted
import com.memo.memories.generation.api.service.GeneratingMemoryService

@Composable
fun MemoriesAutoGenerationCheck(
    generationRequired: Boolean,
) {
    var permissionGranted by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    if (generationRequired) {
        CheckForImagesReadPermission {
            permissionGranted = true
        }
    }

    LaunchedEffect(generationRequired, permissionGranted) {
        val isPermissionGranted = isStoragePermissionsGranted(context)
        if (!isPermissionGranted) {
            // TODO show dialog
        } else if (generationRequired) {
            context.startService(Intent(context, GeneratingMemoryService::class.java))
        }
    }
}
