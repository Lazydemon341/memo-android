package com.memo.core.design.icons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.memo.core.design.text.MemoAdditionalText

@Composable
fun MemoIconButtonWithLabel(
    imageVector: ImageVector,
    label: String,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    MemoIconWithLabel(
        Icon = {
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = onClick,
            ) {
                Icon(
                    imageVector = imageVector,
                    tint = Color.White,
                    contentDescription = contentDescription,
                )
            }
        },
        label = label,
        modifier = modifier,
    )
}

@Composable
fun MemoIconToggleButtonWithLabel(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    imageVectorByStateProvider: (Boolean) -> ImageVector,
    tint: Color,
    label: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    MemoIconWithLabel(
        Icon = {
            MemoIconToggleButton(
                checked = checked,
                onCheckedChange = onCheckedChange,
                imageVectorByStateProvider = imageVectorByStateProvider,
                tint = tint,
                modifier = modifier,
                contentDescription = contentDescription,
            )
        },
        label = label,
        modifier = modifier,
    )
}

@Composable
fun MemoIconWithLabel(
    imageVector: ImageVector,
    label: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    MemoIconWithLabel(
        Icon = {
            Icon(
                modifier = modifier,
                imageVector = imageVector,
                tint = Color.White,
                contentDescription = contentDescription,
            )
        },
        label = label,
        modifier = modifier,
    )
}

@Composable
fun MemoIconWithLabel(
    Icon: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon()
        MemoAdditionalText(
            modifier = Modifier.padding(start = 4.dp),
            text = label,
        )
    }
}
