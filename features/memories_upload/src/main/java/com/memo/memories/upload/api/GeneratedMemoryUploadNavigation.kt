package com.memo.memories.upload.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.memo.core.model.memories.upload.NavigateToMemoryUploadArguments
import com.memo.memories.upload.internal.MemoryRoute

const val memoryIdNavArgument = "memory_id"

const val memoryUploadNavigationRoute = "memory_upload/{$memoryIdNavArgument}"

fun NavController.navigateToMemoryUpload(
    arguments: NavigateToMemoryUploadArguments,
    navOptions: NavOptions? = null
) {
    this.navigate(
        memoryUploadNavigationRoute.replace(
            "{$memoryIdNavArgument}",
            arguments.memoryId.toString()
        ),
        navOptions
    )
}

fun NavGraphBuilder.memoryUpload(
    onBackPressed: () -> Unit,
) {
    composable(
        route = memoryUploadNavigationRoute,
        arguments = listOf(
            navArgument(memoryIdNavArgument) {
                type = NavType.LongType
            }
        )
    ) { backStackEntry ->
        MemoryRoute(
            memoryId = backStackEntry.arguments?.getLong(memoryIdNavArgument) ?: 0L,
            onBackPressed = onBackPressed
        )
    }
}
