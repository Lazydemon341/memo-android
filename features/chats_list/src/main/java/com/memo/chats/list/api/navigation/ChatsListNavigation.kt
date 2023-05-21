package com.memo.chats.list.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.memo.chats.list.internal.navigation.ChatsListRoute
import com.memo.core.model.messaging.NavigateToChatArgs

const val chatsListNavigationRoute = "chats_list_route"

fun NavController.navigateToChatsList(navOptions: NavOptions? = null) {
    this.navigate(chatsListNavigationRoute, navOptions)
}

fun NavGraphBuilder.chatsListScreen(
    onBackPressed: () -> Unit,
    onNavigateToChat: (NavigateToChatArgs) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = chatsListNavigationRoute) {
        ChatsListRoute(
            onBackPressed = onBackPressed,
            onNavigateToChat = onNavigateToChat,
        )
    }
    nestedGraphs()
}
