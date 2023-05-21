package com.memo.memories.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.memo.memories.internal.ui.MemoryRoute

const val memoryIdNavArgument = "memory_id"

const val memoryNavigationRoute = "memory_route/{$memoryIdNavArgument}"

fun NavController.navigateToMemory(memoryId: Long, navOptions: NavOptions? = null) {
    this.navigate(
        route = memoryNavigationRoute
            .replace("{$memoryIdNavArgument}", memoryId.toString()),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.memoryScreen(onBackPressed: () -> Unit) {
    composable(
        route = memoryNavigationRoute,
        arguments = listOf(
            navArgument(memoryIdNavArgument) {
                type = NavType.LongType
            },
        )
    ) {
        MemoryRoute(onBackPressed = onBackPressed)
    }
}
