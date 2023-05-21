package com.memo.feed.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.memo.feed.internal.navigation.FeedRoute

const val feedNavigationRoute = "feed_route"

fun NavController.navigateToFeed(navOptions: NavOptions? = null) {
    this.navigate(feedNavigationRoute, navOptions)
}

fun NavGraphBuilder.feedScreen(
    onBackPressed: () -> Unit,
    onNavigateToUserProfile: (Long) -> Unit,
) {
    composable(route = feedNavigationRoute) {
        FeedRoute(onBackPressed = onBackPressed, onNavigateToUserProfile = onNavigateToUserProfile)
    }
}
