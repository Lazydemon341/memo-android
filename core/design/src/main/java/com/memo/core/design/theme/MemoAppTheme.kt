package com.memo.core.design.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MemoAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MemoAppDarkColorScheme,
        content = content
    )
}
