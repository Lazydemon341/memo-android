package com.memo.camera.internal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.memo.camera.internal.ui.CameraPermission
import com.memo.camera.internal.ui.CameraScreen
import com.memo.camera.internal.ui.CameraViewModel
import kotlinx.coroutines.launch

@Composable
internal fun CameraRoute(
    onNavigateToPublishPhoto: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    CameraScreen(
        onPhotoTaken = { bitmap ->
            coroutineScope.launch {
                // TODO: move launch to viewModel, add state handling
                viewModel.onPhotoTaken(bitmap)
                onNavigateToPublishPhoto()
            }
        },
    )
    CameraPermission()
}
