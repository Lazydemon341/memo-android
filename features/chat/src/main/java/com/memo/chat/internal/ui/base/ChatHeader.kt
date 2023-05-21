package com.memo.chat.internal.ui.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memo.core.design.theme.MemoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatHeader(
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
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "",
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ChatHeaderPreview() {
    MemoAppTheme {
        ChatHeader(
            title = "Preview!",
            onNavIconPressed = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        )
    }
}
