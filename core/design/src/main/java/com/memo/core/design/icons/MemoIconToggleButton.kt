package com.memo.core.design.icons

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MemoIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    imageVectorByStateProvider: (Boolean) -> ImageVector,
    tint: Color,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        modifier = modifier.size(24.dp),
        checked = checked,
        onCheckedChange = onCheckedChange,
    ) {
        Crossfade(targetState = checked) {
            Icon(
                imageVector = imageVectorByStateProvider(it),
                tint = tint,
                contentDescription = contentDescription,
            )
        }
    }
}
