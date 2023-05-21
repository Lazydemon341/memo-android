package com.memo.chats.list.internal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memo.chats.list.internal.ui.model.ChatUiState
import com.memo.chats.list.internal.ui.model.LastMessageUiState
import com.memo.core.design.pull.refresh.PullRefreshIndicator
import com.memo.core.design.pull.refresh.pullRefresh
import com.memo.core.design.pull.refresh.rememberPullRefreshState
import com.memo.core.design.theme.MemoAppTheme
import com.memo.core.model.messaging.NavigateToChatArgs
import com.memo.features.chats_list.R.string
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatsListScreen(
    onBackPressed: () -> Unit,
    chatsProvider: () -> List<ChatUiState>,
    updateChats: () -> Unit,
    loadingStateProvider: () -> Boolean,
    onChatClick: (NavigateToChatArgs) -> Unit,
) {
    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ChatsListHeader(
                title = stringResource(id = string.chats_list_header_title),
                scrollBehavior = scrollBehavior,
                onNavIconPressed = onBackPressed,
            )
        },
    ) { paddingValues ->
        ChatsList(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            scrollState = scrollState,
            chatsProvider = chatsProvider,
            updateChats = updateChats,
            loadingStateProvider = loadingStateProvider,
            onChatClick = onChatClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatsListHeader(
    title: String,
    onNavIconPressed: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable(onClick = onNavIconPressed),
                imageVector = Filled.ArrowBack,
                contentDescription = "",
            )
        }
    )
}

private const val ChatContentType = "chat"
private const val ChatDividerContentType = "chat_divider"

@Composable
private fun ChatsList(
    chatsProvider: () -> List<ChatUiState>,
    updateChats: () -> Unit,
    loadingStateProvider: () -> Boolean,
    onChatClick: (NavigateToChatArgs) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        updateChats()
    }
    val pullRefreshState = rememberPullRefreshState(loadingStateProvider(), updateChats)
    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState),
    ) {
        LazyColumn(
            modifier = Modifier
                .matchParentSize(),
            state = scrollState,
        ) {
            val chats = chatsProvider()
            chats.forEachIndexed { index, chat ->
                item(
                    key = chat.id,
                    contentType = ChatContentType,
                ) {
                    Chat(
                        modifier = Modifier
                            .clickable {
                                onChatClick(
                                    NavigateToChatArgs(
                                        chatId = chat.id,
                                        chatTitle = chat.title,
                                        interlocutorEmail = chat.interlocutorEmail,
                                    )
                                )
                            },
                        chat = chat,
                    )
                }
                if (index != chats.lastIndex) {
                    item(contentType = ChatDividerContentType) {
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(start = 88.dp, end = 16.dp),
                            color = Color.Gray,
                            thickness = 0.3.dp,
                        )
                    }
                }
            }
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = loadingStateProvider(),
            state = pullRefreshState,
        )
    }
}

@Preview
@Composable
private fun ChatsListScreenPreview() {
    MemoAppTheme {
        ChatsListScreen(
            onBackPressed = {},
            chatsProvider = { previewChats },
            loadingStateProvider = { false },
            updateChats = {},
            onChatClick = {},
        )
    }
}

private val previewChats = listOf(
    ChatUiState(
        title = "Бульдозер Саня",
        id = Random.nextLong(),
        photoUrl = "https://www.linkpicture.com/q/2022-08-25-22.22.43.jpg",
        lastMessage = LastMessageUiState(
            content = "Привет! Выложил пр ленты. Посмотри, пожалуйста, как будет время.",
            sentTime = "4:20",
        ),
        interlocutorEmail = "",
    ),
    ChatUiState(
        title = "Бульдозер Саня",
        id = Random.nextLong(),
        photoUrl = "https://www.linkpicture.com/q/2022-08-25-22.22.43.jpg",
        lastMessage = LastMessageUiState(
            content = "Привет! Выложил пр ленты. Посмотри, пожалуйста, как будет время.",
            sentTime = "4:20",
        ),
        interlocutorEmail = "",
    ),
)
