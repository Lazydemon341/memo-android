package com.memo.chat.internal.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.memo.chat.internal.ui.base.ChatHeader
import com.memo.chat.internal.ui.base.Messages
import com.memo.chat.internal.ui.base.UserInput
import com.memo.chat.internal.ui.model.MessageUiState
import com.memo.core.design.theme.MemoAppTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
internal fun ChatScreen(
    onNavIconPressed: () -> Unit,
    navigateToProfile: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val messages = viewModel.messagesPagingFlow.collectAsLazyPagingItems()
    val title = viewModel.title
    ChatScreenContent(
        messages = messages,
        title = title,
        onNavIconPressed = onNavIconPressed,
        navigateToProfile = navigateToProfile,
        onSendMessage = viewModel::sendMessage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreenContent(
    messages: LazyPagingItems<MessageUiState>,
    title: String,
    onNavIconPressed: () -> Unit,
    navigateToProfile: (String) -> Unit,
    onSendMessage: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ChatHeader(
                modifier = modifier,
                title = title,
                scrollBehavior = scrollBehavior,
                onNavIconPressed = onNavIconPressed,
            )
        },
        // Exclude ime and navigation bar padding so this can be added by the UserInput composable
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Messages(
                pagingMessages = messages,
                navigateToProfile = navigateToProfile,
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            UserInput(
                onSendMessage = {
                    scope.launch {
                        onSendMessage(it)
                        messages.refresh()
                    }
                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    MemoAppTheme {
        ChatScreenContent(
            title = "Anas Ben Mustafa",
            onNavIconPressed = {},
            navigateToProfile = {},
            messages = flowOf(PagingData.from(previewMessages)).collectAsLazyPagingItems(),
            onSendMessage = {},
        )
    }
}

private const val EMOJI_CAT_HEART_EYES = "\uD83D\uDE3B"
private const val EMOJI_MELTING = "\uD83E\uDEE0"
private const val EMOJI_CLOUDS = "\uD83D\uDE36\u200D\uD83C\uDF2B️"
private const val EMOJI_FLAMINGO = "\uD83E\uDDA9"
private const val EMOJI_POINTS = " \uD83D\uDC49"

private val previewMessages = listOf(
    MessageUiState(
        false,
        "Check it out!",
        id = Random.nextLong(),
        sentDate = "21 Aug",
        sentTime = "8:07 PM",
        authorImageUrl = "https://images.app.goo.gl/AdYZNAvQurrWWL4V6",
    ),
    MessageUiState(
        false,
        "Thank you!$EMOJI_CAT_HEART_EYES",
        id = Random.nextLong(),
        sentDate = "20 Aug",
        sentTime = "8:06 PM",
        authorImageUrl = null,
    ),
    MessageUiState(
        false,
        "You can use all the same stuff",
        id = Random.nextLong(),
        sentDate = "20 Aug",
        sentTime = "8:06 PM",
        authorImageUrl = "https://www.linkpicture.com/q/2022-08-25-22.22.43.jpg",
    ),
    MessageUiState(
        false,
        "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
        id = Random.nextLong(),
        sentDate = "20 Aug",
        sentTime = "8:05 PM",
        authorImageUrl = "https://www.linkpicture.com/q/2022-08-25-22.22.43.jpg",
    ),
    MessageUiState(
        false,
        "Compose newbie as well $EMOJI_FLAMINGO, have you looked at the JetNews sample? " +
            "Most blog posts end up out of date pretty fast but this sample is always up to " +
            "date and deals with async data loading (it's faked but the same idea " +
            "applies) $EMOJI_POINTS https://goo.gle/jetnews",
        id = Random.nextLong(),
        sentDate = "20 Aug",
        sentTime = "8:04 PM",
        authorImageUrl = "https://www.linkpicture.com/q/2022-08-25-22.22.43.jpg",
    ),
    MessageUiState(
        true,
        "Compose newbie: I’ve scourged the internet for tutorials about async data " +
            "loading but haven’t found any good ones $EMOJI_MELTING $EMOJI_CLOUDS. " +
            "What’s the recommended way to load async data and emit composable widgets?",
        id = Random.nextLong(),
        sentDate = "19 Aug",
        sentTime = "8:04 PM",
        "8:03 PM"
    )
)
