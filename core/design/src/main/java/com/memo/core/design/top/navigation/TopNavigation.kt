package com.memo.core.design.top.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memo.core.design.icons.Icon
import com.memo.core.design.icons.MemoIcon

private val TopRowNavigationHeight = 48.dp
private val IconSize = 32.dp

data class TopNavigationTabInfo(
    val isSelected: Boolean,
    val text: String,
    val onClick: () -> Unit
)

data class TopNavigationIconInfo(
    val icon: Icon,
    val onClick: () -> Unit
)

@Composable
fun TopNavigation(
    navigationTabs: List<TopNavigationTabInfo>,
    profileDestination: TopNavigationIconInfo,
    friendsDestination: TopNavigationIconInfo,
    messengerDestination: TopNavigationIconInfo,
    memoriesDestination: TopNavigationIconInfo,
    mapDestination: TopNavigationIconInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1C))
    ) {
        TopRowNavigation(
            profileDestination = profileDestination,
            messengerDestination = messengerDestination,
            mapDestination = mapDestination,
            friendsDestination = friendsDestination,
            memoriesDestination = memoriesDestination,
        )
        Spacer(Modifier.height(8.dp))
        TabsRowNavigation(navigationTabs = navigationTabs)
    }
}

@Composable
private fun TopRowNavigation(
    profileDestination: TopNavigationIconInfo,
    friendsDestination: TopNavigationIconInfo,
    messengerDestination: TopNavigationIconInfo,
    mapDestination: TopNavigationIconInfo,
    memoriesDestination: TopNavigationIconInfo,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Box(modifier = Modifier.weight(1f, true)) {
            MemoIcon(
                profileDestination.icon,
                contentDescription = null,
                modifier = Modifier
                    .size(IconSize)
                    .clip(CircleShape)
                    .clickable {
                        profileDestination.onClick()
                    }
                    .align(Alignment.CenterStart)
            )
        }
        Box(modifier = Modifier.weight(1f, true)) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MemoIcon(
                    memoriesDestination.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(IconSize)
                        .padding(2.dp)
                        .clickable { memoriesDestination.onClick() }
                )
                MemoIcon(
                    mapDestination.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(IconSize)
                        .padding(2.dp)
                        .clickable { mapDestination.onClick() }
                )
                MemoIcon(
                    friendsDestination.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(IconSize)
                        .clip(CircleShape)
                        .clickable {
                            friendsDestination.onClick()
                        }
                )
                MemoIcon(
                    messengerDestination.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(IconSize)
                        .padding(2.dp)
                        .clickable { messengerDestination.onClick() }
                )
            }
        }
    }
}

@Composable
private fun TabsRowNavigation(
    navigationTabs: List<TopNavigationTabInfo>,
) {
    val selectedTabIndex =
        navigationTabs.indexOfFirst { it.isSelected }.coerceIn(0, navigationTabs.lastIndex)
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        indicator = {},
        divider = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(TopRowNavigationHeight)
    ) {
        navigationTabs.forEachIndexed { index, tabInfo ->
            val isSelected = index == selectedTabIndex
            Tab(
                selected = isSelected,
                onClick = tabInfo.onClick,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = tabInfo.text,
                    color = if (isSelected) {
                        Color.White
                    } else {
                        Color.Gray
                    },
                    fontSize = if (isSelected) {
                        28.sp
                    } else {
                        20.sp
                    },
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}
