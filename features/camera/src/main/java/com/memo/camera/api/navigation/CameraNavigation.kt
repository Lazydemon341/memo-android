package com.memo.camera.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.memo.camera.internal.navigation.CameraRoute

const val cameraNavigationRoute = "camera_route"

fun NavController.navigateToCamera(navOptions: NavOptions? = null) {
    this.navigate(cameraNavigationRoute, navOptions)
}

fun NavGraphBuilder.cameraScreen(
    onNavigateToPublishPhoto: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = cameraNavigationRoute) {
        CameraRoute(
            onNavigateToPublishPhoto = onNavigateToPublishPhoto,
        )
    }
    nestedGraphs()
}
