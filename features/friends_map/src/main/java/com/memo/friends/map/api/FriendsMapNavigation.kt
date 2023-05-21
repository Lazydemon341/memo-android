package com.memo.friends.map.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.memo.core.model.map.NavigateToPostArguments
import com.memo.friends.map.internal.FriendsMapRoute

const val friendsMapNavigationRoute = "friends_map"

fun NavController.navigateToFriendsMap(navOptions: NavOptions? = null) {
    this.navigate(friendsMapNavigationRoute, navOptions)
}

fun NavGraphBuilder.friendsMapScreen(
    onBackPressed: () -> Unit,
    onNavigateToPost: (NavigateToPostArguments) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = friendsMapNavigationRoute) {
        FriendsMapRoute(
            onBackPressed = onBackPressed,
            navigateToPost = onNavigateToPost,
        )
    }
    nestedGraphs()
}
