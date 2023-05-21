package com.memo.friendship.internal

import androidx.compose.runtime.Composable
import com.memo.friendship.internal.ui.FriendshipScreen

@Composable
internal fun FriendshipRoute(
    onBackPressed: () -> Unit,
    onNavigateToUserProfile: (Long) -> Unit,
) {
    FriendshipScreen(
        onBackPressed = onBackPressed,
        onOpenFriendProfile = onNavigateToUserProfile,
    )
}
