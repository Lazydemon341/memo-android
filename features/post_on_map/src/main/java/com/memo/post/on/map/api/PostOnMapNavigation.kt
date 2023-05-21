package com.memo.post.on.map.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.memo.core.model.map.NavigateToPostArguments
import com.memo.post.on.map.internal.PostOnMap

const val postIdNavArgument = "post_id"

const val postOnMapNavigationRoute = "post_on_map/{$postIdNavArgument}"

fun NavController.navigateToPostOnMap(
    arguments: NavigateToPostArguments,
    navOptions: NavOptions? = null
) {
    this.navigate(
        postOnMapNavigationRoute.replace(
            "{$postIdNavArgument}",
            arguments.postId.toString()
        ),
        navOptions
    )
}

fun NavGraphBuilder.postOnMap(
    onBackPressed: () -> Unit,
) {
    composable(
        route = postOnMapNavigationRoute,
        arguments = listOf(
            navArgument(postIdNavArgument) {
                type = NavType.LongType
            }
        )
    ) { backStackEntry ->
        PostOnMap(
            postId = backStackEntry.arguments?.getLong(postIdNavArgument) ?: 0L,
            onBackPressed
        )
    }
}
