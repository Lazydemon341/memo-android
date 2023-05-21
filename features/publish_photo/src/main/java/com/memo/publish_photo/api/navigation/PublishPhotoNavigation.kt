package com.memo.publish_photo.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.memo.publish_photo.internal.ui.PublishPhotoRoute

private const val publishPhotoNavigationRoute = "publish_photo_route"

fun NavController.navigateToPublishPhoto(navOptions: NavOptions? = null) {
    this.navigate(publishPhotoNavigationRoute, navOptions)
}

fun NavGraphBuilder.publishPhotoScreen(onBackPressed: () -> Unit) {
    composable(route = publishPhotoNavigationRoute) {
        PublishPhotoRoute(onBackPressed = onBackPressed)
    }
}
