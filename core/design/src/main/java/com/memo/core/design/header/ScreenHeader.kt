package com.memo.core.design.header

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.memo.core.design.icons.MemoIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHeader(
    title: String,
    onBackPressed: () -> Unit,
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        colors =
        TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        actions = actions,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    MemoIcons.ArrowBack,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "",
                )
            }
        },
    )
}
