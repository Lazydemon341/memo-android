package com.memo.core.design.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MemoBigTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 48.sp,
    )
}

@Composable
fun MemoTitleWithDivider(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MemoRegularText(text = text)
        Divider(
            modifier = Modifier.padding(start = 8.dp),
            color = Color.White,
            thickness = 1.dp,
        )
    }
}

@Composable
fun MemoSmallTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )
}

@Composable
fun MemoMainText(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        color = Color.White,
        fontSize = 24.sp,
    )
}

@Composable
fun MemoRegularText(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        color = Color.White,
        fontSize = 18.sp,
    )
}

@Composable
fun MemoAdditionalText(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        color = Color.White,
        fontSize = 12.sp,
    )
}

@Composable
fun MemoCommentText(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        color = Color.Gray,
        fontSize = 12.sp,
    )
}
