package com.memo.chat.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.memo.chat.internal.ui.ChatRoute
import com.memo.core.model.messaging.NavigateToChatArgs

const val chatIdNavArgument = "chat_id"
const val chatTitleNavArgument = "chat_title"
const val chatInterlocutorEmailNavArgument = "chat_interlocutor_email"

const val chatNavigationRoute = "chat_route" +
    "/{$chatIdNavArgument}" +
    "/{$chatTitleNavArgument}" +
    "/{$chatInterlocutorEmailNavArgument}"

fun NavController.navigateToChat(
    arguments: NavigateToChatArgs,
    navOptions: NavOptions? = null,
) {
    val route = chatNavigationRoute
        .replace("{$chatIdNavArgument}", arguments.chatId.toString())
        .replace("{$chatTitleNavArgument}", arguments.chatTitle)
        .replace("{$chatInterlocutorEmailNavArgument}", arguments.interlocutorEmail)
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.chatScreen(onBackPressed: () -> Unit) {
    composable(
        route = chatNavigationRoute,
        arguments = listOf(
            navArgument(chatIdNavArgument) {
                type = NavType.LongType
            },
            navArgument(chatTitleNavArgument) {
                type = NavType.StringType
            },
            navArgument(chatInterlocutorEmailNavArgument) {
                type = NavType.StringType
            },
        )
    ) {
        ChatRoute(onBackPressed = onBackPressed)
    }
}
