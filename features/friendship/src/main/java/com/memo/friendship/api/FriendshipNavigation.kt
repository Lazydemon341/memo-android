package com.memo.friendship.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.memo.friendship.internal.FriendshipRoute

const val friendshipNavigationRoute = "friendship"

fun NavController.navigateToFriendship(navOptions: NavOptions? = null) {
    this.navigate(friendshipNavigationRoute, navOptions)
}

fun NavGraphBuilder.friendshipScreen(
    onBackPressed: () -> Unit,
    onNavigateToUserProfile: (Long) -> Unit,
) {
    composable(route = friendshipNavigationRoute) {
        FriendshipRoute(
            onBackPressed = onBackPressed,
            onNavigateToUserProfile = onNavigateToUserProfile,
        )
    }
}
