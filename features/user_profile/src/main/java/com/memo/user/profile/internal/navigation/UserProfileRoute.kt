package com.memo.user.profile.internal.navigation

import androidx.compose.runtime.Composable
import com.memo.user.profile.internal.ui.UserProfileScreen

@Composable
internal fun UserProfileRoute(
    onBackClick: () -> Unit,
    onNavigateToMemory: (Long) -> Unit,
) {
    UserProfileScreen(
        onBackClick = onBackClick,
        onMemoryClick = onNavigateToMemory,
    )
}
