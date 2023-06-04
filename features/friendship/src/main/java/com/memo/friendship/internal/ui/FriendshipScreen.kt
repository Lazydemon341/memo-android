package com.memo.friendship.internal.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.memo.core.design.pull.refresh.PullRefreshIndicator
import com.memo.core.design.pull.refresh.pullRefresh
import com.memo.core.design.pull.refresh.rememberPullRefreshState
import com.memo.core.design.text.MemoCommentText
import com.memo.core.design.text.MemoSmallTitle
import com.memo.core.design.theme.MemoAppTheme
import com.memo.features.friendship.R
import com.memo.friendship.internal.ui.friends.FriendContent
import com.memo.friendship.internal.ui.friends.FriendsUiState
import com.memo.friendship.internal.ui.requests.RequestContent
import com.memo.friendship.internal.ui.requests.RequestsUiState
import com.memo.friendship.internal.ui.requests.RequestsUiState.Error
import com.memo.friendship.internal.ui.requests.RequestsUiState.Loading
import com.memo.friendship.internal.ui.requests.RequestsUiState.Ready

@Composable
internal fun FriendshipScreen(
    onBackPressed: () -> Unit,
    onOpenFriendProfile: (Long) -> Unit,
    viewModel: FriendshipViewModel = hiltViewModel(),
) {
    val requestsUiState = viewModel.requestsUiState.collectAsStateWithLifecycle()
    val friendsUiState = viewModel.friendsUiState.collectAsStateWithLifecycle()
    val updatingState = viewModel.updatingState.collectAsStateWithLifecycle()
    val qrState = viewModel.friendshipQRState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.updateFriendsAndRequests()
    }
    FriendshipScreenScaffold(
        requestsUiStateProvider = requestsUiState::value,
        friendsUiStateProvider = friendsUiState::value,
        updatingStateProvider = updatingState::value,
        onAcceptRequest = viewModel::acceptRequest,
        onDeclineRequest = viewModel::declineRequest,
        onUpdateData = viewModel::updateFriendsAndRequests,
        onBackPressed = onBackPressed,
        friendshipQRProvider = qrState::value,
        onOpenFriendProfile = onOpenFriendProfile,
        onDeleteFriend = viewModel::deleteFriend,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FriendshipScreenScaffold(
    requestsUiStateProvider: () -> RequestsUiState,
    friendsUiStateProvider: () -> FriendsUiState,
    updatingStateProvider: () -> Boolean,
    onOpenFriendProfile: (Long) -> Unit,
    onAcceptRequest: (Long) -> Unit,
    onDeclineRequest: (Long) -> Unit,
    onUpdateData: () -> Unit,
    onBackPressed: () -> Unit,
    friendshipQRProvider: () -> Bitmap?,
    onDeleteFriend: (Long) -> Unit,
) {
    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    var showRequests by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = onBackPressed),
                        imageVector = Filled.ArrowBack,
                        contentDescription = "",
                    )
                },
                actions = {
                    Text(
                        modifier = Modifier
                            .clickable { showRequests = !showRequests },
                        text = stringResource(
                            id = if (showRequests) R.string.show_friends_button else R.string.show_requests_button
                        ),
                    )
                }
            )
        },
    ) { paddingValues ->
        FriendshipScreenContent(
            modifier = Modifier.padding(paddingValues),
            requestsUiStateProvider = requestsUiStateProvider,
            friendsUiStateProvider = friendsUiStateProvider,
            updatingStateProvider = updatingStateProvider,
            showRequestsProvider = { showRequests },
            onAcceptRequest = onAcceptRequest,
            onDeclineRequest = onDeclineRequest,
            onUpdateData = onUpdateData,
            lazyListState = scrollState,
            friendshipQRProvider = friendshipQRProvider,
            onOpenFriendProfile = onOpenFriendProfile,
            onDeleteFriend = onDeleteFriend,
        )
    }
}

private const val TitleContentType = "title"
private const val FriendContentType = "friend"
private const val RequestContentType = "request"

@Composable
private fun FriendshipScreenContent(
    requestsUiStateProvider: () -> RequestsUiState,
    friendsUiStateProvider: () -> FriendsUiState,
    updatingStateProvider: () -> Boolean,
    showRequestsProvider: () -> Boolean,
    onAcceptRequest: (Long) -> Unit,
    onDeclineRequest: (Long) -> Unit,
    onUpdateData: () -> Unit,
    lazyListState: LazyListState,
    friendshipQRProvider: () -> Bitmap?,
    onOpenFriendProfile: (Long) -> Unit,
    onDeleteFriend: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshing by remember {
        derivedStateOf {
            updatingStateProvider() ||
                requestsUiStateProvider() == RequestsUiState.Loading ||
                friendsUiStateProvider() == FriendsUiState.Loading
        }
    }
    val pullRefreshState =
        rememberPullRefreshState(refreshing = refreshing, onRefresh = onUpdateData)
    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item(contentType = TitleContentType) {
                    MemoSmallTitle(
                        text = if (showRequestsProvider()) {
                            stringResource(R.string.requests_title)
                        } else {
                            stringResource(R.string.friends_title)
                        },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (showRequestsProvider()) {
                    when (val requestsUiState = requestsUiStateProvider()) {
                        is Ready -> {
                            items(
                                items = requestsUiState.requests,
                                key = { it.id },
                                contentType = { RequestContentType },
                            ) { request ->
                                RequestContent(
                                    modifier = Modifier.fillMaxWidth(),
                                    request = request,
                                    onAccept = onAcceptRequest,
                                    onDecline = onDeclineRequest,
                                )
                            }
                        }

                        is Loading -> {}
                        is Error -> {
                            // TODO()
                        }
                    }
                } else {
                    when (val friendsUiState = friendsUiStateProvider()) {
                        is FriendsUiState.Ready -> {
                            items(
                                items = friendsUiState.friends,
                                key = { it.id },
                                contentType = { FriendContentType },
                            ) {
                                FriendContent(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onOpenFriendProfile(it.id) },
                                    friend = it,
                                    onOpenFriendProfile = onOpenFriendProfile,
                                    onDeleteFriend = onDeleteFriend,
                                )
                            }
                        }

                        is FriendsUiState.Loading -> {}
                        is FriendsUiState.Error -> {
                            // TODO()
                        }
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color.White,
                thickness = 1.dp,
            )
            FriendshipQRImage(
                modifier = Modifier.fillMaxWidth(),
                friendshipQRProvider = friendshipQRProvider,
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter),
            refreshing = refreshing,
            state = pullRefreshState,
        )
    }
}

@Composable
private fun FriendshipQRImage(
    friendshipQRProvider: () -> Bitmap?,
    modifier: Modifier = Modifier,
) {
    friendshipQRProvider()?.let { qr ->
        Column(
            modifier = modifier,
        ) {
            MemoSmallTitle(text = stringResource(R.string.friendship_qr_title))
            MemoCommentText(
                text = stringResource(R.string.friendship_qr_subtitle),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(200.dp)
                    .clip(RoundedCornerShape(20.dp)),
                bitmap = qr.asImageBitmap(),
                contentDescription = "",
            )
        }
    }
}

@Composable
@Preview
private fun FriendshipScreenPreview() {
    MemoAppTheme {
        FriendshipScreenScaffold(
            requestsUiStateProvider = { RequestsUiState.Loading },
            friendsUiStateProvider = { FriendsUiState.Loading },
            updatingStateProvider = { false },
            onAcceptRequest = {},
            onDeclineRequest = {},
            onUpdateData = {},
            onBackPressed = {},
            friendshipQRProvider = { null },
            onOpenFriendProfile = {},
            onDeleteFriend = {},
        )
    }
}
