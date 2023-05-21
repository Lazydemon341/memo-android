package com.memo.core.design.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun MemoInputText(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = { },
    trailingIcon: @Composable () -> Unit = { },
    placeholderText: String = "",
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
) {
    var text by rememberSaveable { mutableStateOf("") }
    BasicTextField(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                MaterialTheme.shapes.small,
            )
            .fillMaxWidth(),
        value = text,
        onValueChange = { text = it },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.surface,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) {
                        Text(
                            placeholderText,
                            style = LocalTextStyle.current.copy(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                fontSize = fontSize
                            )
                        )
                    }
                    innerTextField()
                }
                trailingIcon()
            }
        }
    )
}
