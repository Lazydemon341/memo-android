package com.memo.core.design.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun MemoIcon(
    icon: Icon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    when (icon) {
        is Icon.ImageVectorIcon -> {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }
        is Icon.DrawableResourceIcon -> {
            Icon(
                painter = painterResource(icon.id),
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }
    }
}
