package com.memo.core.design.icons

import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MemoLikeIcon(
    liked: Boolean,
    onClick: (Boolean) -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    MemoIconToggleButton(
        checked = liked,
        onCheckedChange = onClick,
        modifier = modifier,
        imageVectorByStateProvider = { checked ->
            if (checked) Filled.Favorite else Outlined.FavoriteBorder
        },
        tint = Color.White,
        contentDescription = contentDescription,
    )
}

@Composable
fun MemoLikeIcon(
    liked: Boolean,
    onClick: (Boolean) -> Unit,
    label: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    MemoIconToggleButtonWithLabel(
        checked = liked,
        onCheckedChange = onClick,
        modifier = modifier,
        label = label,
        imageVectorByStateProvider = { checked ->
            if (checked) Filled.Favorite else Outlined.FavoriteBorder
        },
        tint = Color.White,
        contentDescription = contentDescription,
    )
}
