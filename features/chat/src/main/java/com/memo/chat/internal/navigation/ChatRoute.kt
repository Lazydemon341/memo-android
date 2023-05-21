package com.memo.chat.internal.ui

import androidx.compose.runtime.Composable

@Composable
internal fun ChatRoute(
    onBackPressed: () -> Unit,
) {
    ChatScreen(
        onNavIconPressed = onBackPressed,
        navigateToProfile = {},
    )
}
