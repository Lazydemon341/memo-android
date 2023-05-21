package com.memo.camera.internal.ui.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memo.core.design.theme.MemoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CameraScreenTopBar(
    onChatIconClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {},
        actions = {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onChatIconClicked() },
                imageVector = Filled.Message,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = ""
            )
        }
    )
}

@Composable
@Preview
private fun CameraScreenTopBarPreview() {
    MemoAppTheme {
        CameraScreenTopBar(
            onChatIconClicked = {},
        )
    }
}
