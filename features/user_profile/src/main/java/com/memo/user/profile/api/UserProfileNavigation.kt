package com.memo.user.profile.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.memo.user.profile.internal.navigation.UserProfileRoute

const val userProfileIDNavArgument = "user_id"

const val userProfileNavigationRoute = "user_profile/{$userProfileIDNavArgument}"

fun NavController.navigateToUserProfile(userId: Long? = null, navOptions: NavOptions? = null) {
    this.navigate(
        route = userProfileNavigationRoute
            .replace("{$userProfileIDNavArgument}", userId.toString()),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.userProfileScreen(
    onBackPressed: () -> Unit,
    onNavigateToMemory: (Long) -> Unit,
) {
    composable(
        route = userProfileNavigationRoute,
        arguments = listOf(
            navArgument(userProfileIDNavArgument) {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        UserProfileRoute(onBackClick = onBackPressed, onNavigateToMemory = onNavigateToMemory)
    }
}
