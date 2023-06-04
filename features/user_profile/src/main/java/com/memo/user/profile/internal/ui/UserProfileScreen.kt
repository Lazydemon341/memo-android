package com.memo.user.profile.internal.ui

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.memo.core.design.theme.MemoAppTheme
import com.memo.features.user_profile.R
import com.memo.user.profile.internal.ui.base.CollapsedHeaderContent
import com.memo.user.profile.internal.ui.base.ExtendedHeaderContent
import com.memo.user.profile.internal.ui.base.ProfileMemoriesList
import com.memo.user.profile.internal.ui.model.UserProfileUiState
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy.ExitUntilCollapsed
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
internal fun UserProfileScreen(
    onBackClick: () -> Unit,
    onMemoryClick: (Long) -> Unit,
    viewModel: UserProfileViewModel = hiltViewModel(),
) {
    val userProfileUiState = viewModel.userProfileState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getUserProfile()
    }
    UserProfileScaffold(
        onBackClick = onBackClick,
        profileUiStateProvider = userProfileUiState::value,
        onMemoryClick = onMemoryClick,
        onAvatarChanged = viewModel::avatarChanged,
        onLogout = viewModel::logout,
    )
}

@Composable
private fun UserProfileScaffold(
    onBackClick: () -> Unit,
    profileUiStateProvider: () -> UserProfileUiState,
    onMemoryClick: (Long) -> Unit,
    onAvatarChanged: (Uri) -> Unit,
    onLogout: () -> Unit,
) {
    val toolbarScaffoldState = rememberCollapsingToolbarScaffoldState()
    val postsListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        postsListState.scrollToItem(0)
    }
    CollapsingToolbarScaffold(
        modifier = Modifier,
        state = toolbarScaffoldState,
        scrollStrategy = ExitUntilCollapsed,
        toolbar = {
            CollapsedHeaderContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp)
                    .zIndex(1f),
                collapsedFraction = 1 - toolbarScaffoldState.toolbarState.progress,
                onBackClick = onBackClick,
                onTitleClick = {
                    coroutineScope.launch {
                        postsListState.animateScrollToItem(0)
                    }
                },
                onMoreClick = {
                    showLogoutDialog = true
                },
                profileUiStateProvider = profileUiStateProvider,
            )
            ExtendedHeaderContent(
                modifier = Modifier
                    .fillMaxWidth(),
                extendedFraction = toolbarScaffoldState.toolbarState.progress,
                profileUiStateProvider = profileUiStateProvider,
                onAvatarChanged = onAvatarChanged,
            )
        },
    ) {
        LogoutDialog(
            shouldShow = showLogoutDialog,
            onDismiss = { showLogoutDialog = false },
            onLogout = onLogout,
        )
        ProfileMemoriesList(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            listState = postsListState,
            onMemoryClick = onMemoryClick,
            memoriesProvider = {
                (profileUiStateProvider() as? UserProfileUiState.Ready)?.memories ?: emptyList()
            }
        )
    }
}

@Composable
private fun LogoutDialog(
    shouldShow: Boolean,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (shouldShow) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
            title = {
                Text(text = stringResource(R.string.logout_dialog_title))
            },
            confirmButton = {
                ClickableText(
                    text = AnnotatedString(stringResource(id = android.R.string.ok)),
                    style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onPrimary),
                ) {
                    onDismiss()
                    onLogout()
                }
            },
            dismissButton = {
                ClickableText(
                    text = AnnotatedString(stringResource(id = android.R.string.cancel)),
                    style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onPrimary),
                ) {
                    onDismiss()
                }
            }
        )
    }
}

@Preview
@Composable
fun UserProfilePreview() {
    MemoAppTheme {
        UserProfileScaffold(
            onBackClick = {},
            profileUiStateProvider = { UserProfileUiState.Loading },
            onMemoryClick = {},
            onAvatarChanged = {},
            onLogout = {},
        )
    }
}
