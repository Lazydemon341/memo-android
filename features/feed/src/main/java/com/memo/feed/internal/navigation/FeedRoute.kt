package com.memo.feed.internal.navigation

import androidx.compose.runtime.Composable
import com.memo.feed.internal.ui.FeedScreen

@Composable
internal fun FeedRoute(
    onBackPressed: () -> Unit,
    onNavigateToUserProfile: (Long) -> Unit,
) {
    FeedScreen(onNavigateToUserProfile = onNavigateToUserProfile)
}
