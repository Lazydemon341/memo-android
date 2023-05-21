package com.memo.memories.generation.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.memo.core.model.memories.upload.NavigateToMemoryUploadArguments
import com.memo.memories.generation.internal.list.GeneratedMemoriesListRoute

const val memoriesGenerationNavigationRoute = "memories_generation"

const val memoriesListDeeplink = "deeplink::memories_generation"

fun NavController.navigateToMemoriesAutoGeneration(navOptions: NavOptions? = null) {
    this.navigate(memoriesGenerationNavigationRoute, navOptions)
}

fun NavGraphBuilder.memoriesAutoGeneration(
    onBackPressed: () -> Unit,
    onNavigateToMemoryUpload: (NavigateToMemoryUploadArguments) -> Unit,
) {
    composable(
        route = memoriesGenerationNavigationRoute,
        deepLinks = listOf(navDeepLink { uriPattern = memoriesListDeeplink })
    ) {
        GeneratedMemoriesListRoute(onBackPressed = onBackPressed, navigateToMemory = onNavigateToMemoryUpload)
    }
}
