package com.memo.chats.list.internal.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.memo.chats.list.internal.ui.ChatsListScreen
import com.memo.chats.list.internal.ui.ChatsListViewModel
import com.memo.core.model.messaging.NavigateToChatArgs

@Composable
internal fun ChatsListRoute(
    onBackPressed: () -> Unit,
    onNavigateToChat: (NavigateToChatArgs) -> Unit,
    viewModel: ChatsListViewModel = hiltViewModel(),
) {
    val chatsUiState = viewModel.chatsUiState.collectAsStateWithLifecycle()
    val loadingState = viewModel.loadingState.collectAsStateWithLifecycle()
    ChatsListScreen(
        onBackPressed = onBackPressed,
        chatsProvider = { chatsUiState.value },
        updateChats = viewModel::updateChats,
        loadingStateProvider = { loadingState.value },
        onChatClick = onNavigateToChat,
    )
}
