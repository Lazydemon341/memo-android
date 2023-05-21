package com.memo.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import com.memo.account.api.navigation.navigateToLogin
import com.memo.app.R
import com.memo.app.navigation.AppNavHost
import com.memo.app.navigation.TopLevelDestination
import com.memo.app.navigation.TopLevelDestination.IconTopLevelDestination
import com.memo.app.navigation.TopLevelDestination.TabTopLevelDestination
import com.memo.app.navigation.isTopLevelDestinationInHierarchy
import com.memo.core.datastore.memories.GeneratedMemoriesDataSource
import com.memo.core.datastore.user_tokens.UserTokensDataSource
import com.memo.core.design.top.navigation.TopNavigation
import com.memo.core.design.top.navigation.TopNavigationIconInfo
import com.memo.core.design.top.navigation.TopNavigationTabInfo
import com.memo.core.network.utils.monitor.NetworkMonitor
import com.memo.memories.generation.api.MemoriesAutoGenerationCheck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootComposable(
    networkMonitor: NetworkMonitor,
    userTokensDataSource: UserTokensDataSource,
    generatedMemoriesDataSource: GeneratedMemoriesDataSource,
    appState: AppState = rememberAppState(
        networkMonitor = networkMonitor,
        userTokensDataSource = userTokensDataSource,
        generatedMemoriesDataSource = generatedMemoriesDataSource
    )
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            val shouldShowTopNavigation by appState.shouldShowTopNavigation.collectAsStateWithLifecycle()
            if (shouldShowTopNavigation) {
                MemoTopNavigation(
                    tabDestinations = appState.tabsTopNavigationDestinations,
                    profileDestination = IconTopLevelDestination.Profile,
                    friendsDestination = IconTopLevelDestination.Friends,
                    messengerDestination = IconTopLevelDestination.Messenger,
                    memoriesDestination = IconTopLevelDestination.MemoriesGeneration,
                    mapDestination = IconTopLevelDestination.FriendsMap,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
    ) { padding ->

        val isOffline by appState.isOffline.collectAsStateWithLifecycle()
        val notConnected = stringResource(R.string.snackbar_message_not_connected)
        LaunchedEffect(isOffline) {
            if (isOffline) {
                snackbarHostState.showSnackbar(
                    message = notConnected,
                    duration = SnackbarDuration.Indefinite
                )
            }
        }

        val isAuthorized by appState.isAuthorized.collectAsStateWithLifecycle()
        LaunchedEffect(isAuthorized) {
            if (!isAuthorized) {
                appState.navController.navigateToLogin()
            }
        }

        val memoryGenerationRequired by appState.isMemoryGenerationRequired.collectAsStateWithLifecycle()
        MemoriesAutoGenerationCheck(generationRequired = memoryGenerationRequired)

        AppNavHost(
            navController = appState.navController,
            onBackClick = appState::onBackClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}

@Composable
private fun MemoTopNavigation(
    tabDestinations: List<TabTopLevelDestination>,
    profileDestination: IconTopLevelDestination,
    friendsDestination: IconTopLevelDestination,
    messengerDestination: IconTopLevelDestination,
    mapDestination: IconTopLevelDestination,
    memoriesDestination: IconTopLevelDestination,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    val tabsInfo = tabDestinations.map {
        TopNavigationTabInfo(
            isSelected = currentDestination.isTopLevelDestinationInHierarchy(it),
            text = stringResource(it.textResId),
            onClick = { onNavigateToDestination(it) }
        )
    }
    val profileIconInfo = TopNavigationIconInfo(
        icon = profileDestination.icon,
        onClick = { onNavigateToDestination(profileDestination) },
    )
    val friendsIconInfo = TopNavigationIconInfo(
        icon = friendsDestination.icon,
        onClick = { onNavigateToDestination(friendsDestination) },
    )
    val messengerIconInfo = TopNavigationIconInfo(
        icon = messengerDestination.icon,
        onClick = { onNavigateToDestination(messengerDestination) },
    )
    val mapIconInfo = TopNavigationIconInfo(
        icon = mapDestination.icon,
        onClick = { onNavigateToDestination(mapDestination) },
    )
    val memoriesIconInfo = TopNavigationIconInfo(
        icon = memoriesDestination.icon,
        onClick = { onNavigateToDestination(memoriesDestination) }
    )
    TopNavigation(
        navigationTabs = tabsInfo,
        profileDestination = profileIconInfo,
        friendsDestination = friendsIconInfo,
        messengerDestination = messengerIconInfo,
        mapDestination = mapIconInfo,
        memoriesDestination = memoriesIconInfo,
        modifier = modifier
    )
}
