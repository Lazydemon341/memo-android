package com.memo.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.memo.app.navigation.TopLevelDestination
import com.memo.app.navigation.TopLevelDestination.IconTopLevelDestination
import com.memo.app.navigation.TopLevelDestination.TabTopLevelDestination
import com.memo.camera.api.navigation.cameraNavigationRoute
import com.memo.camera.api.navigation.navigateToCamera
import com.memo.chats.list.api.navigation.navigateToChatsList
import com.memo.core.datastore.memories.GeneratedMemoriesDataSource
import com.memo.core.datastore.user_tokens.UserTokensDataSource
import com.memo.core.network.utils.monitor.NetworkMonitor
import com.memo.feed.api.feedNavigationRoute
import com.memo.feed.api.navigateToFeed
import com.memo.friends.map.api.navigateToFriendsMap
import com.memo.friendship.api.navigateToFriendship
import com.memo.memories.generation.api.navigateToMemoriesAutoGeneration
import com.memo.user.profile.api.navigateToUserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

@Composable
fun rememberAppState(
    networkMonitor: NetworkMonitor,
    userTokensDataSource: UserTokensDataSource,
    generatedMemoriesDataSource: GeneratedMemoriesDataSource,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): AppState {
    return remember(navController, coroutineScope, networkMonitor, userTokensDataSource, generatedMemoriesDataSource) {
        AppState(navController, coroutineScope, networkMonitor, userTokensDataSource, generatedMemoriesDataSource)
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
    userTokensDataSource: UserTokensDataSource,
    generatedMemoriesDataSource: GeneratedMemoriesDataSource,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val tabsTopNavigationDestinations: List<TabTopLevelDestination> = listOf(
        TabTopLevelDestination.Camera,
        TabTopLevelDestination.NewsFeed,
    )

    val shouldShowTopNavigation: StateFlow<Boolean> =
        navController.currentBackStackEntryFlow.map { backStackEntry ->
            backStackEntry.destination.route == feedNavigationRoute ||
                backStackEntry.destination.route == cameraNavigationRoute
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val isAuthorized = userTokensDataSource.tokensFlow
        .map {
            it.refreshToken.isNotEmpty()
        }
        .catch {
            Timber.e(it, "Tokens flow error")
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = true
        )

    val isMemoryGenerationRequired = generatedMemoriesDataSource.isEmpty.combine(isAuthorized) { isEmpty, isAuthorized ->
        isEmpty && isAuthorized
    }.distinctUntilChanged().stateIn(coroutineScope, SharingStarted.Eagerly, false)

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TabTopLevelDestination.Camera -> navController.navigateToCamera(topLevelNavOptions)
            TabTopLevelDestination.NewsFeed -> navController.navigateToFeed(topLevelNavOptions)
            IconTopLevelDestination.Messenger -> navController.navigateToChatsList(topLevelNavOptions)
            IconTopLevelDestination.Profile -> navController.navigateToUserProfile(navOptions = topLevelNavOptions)
            IconTopLevelDestination.FriendsMap -> navController.navigateToFriendsMap(topLevelNavOptions)
            IconTopLevelDestination.Friends -> navController.navigateToFriendship(topLevelNavOptions)
            IconTopLevelDestination.MemoriesGeneration -> navController.navigateToMemoriesAutoGeneration(
                topLevelNavOptions
            )
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}
