package com.memo.app.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.memo.app.R
import com.memo.core.design.icons.Icon
import com.memo.core.design.icons.Icon.ImageVectorIcon
import com.memo.core.design.icons.MemoIcons

sealed class TopLevelDestination(val name: String) {
    sealed class TabTopLevelDestination(
        name: String,
        @StringRes
        val textResId: Int,
    ) : TopLevelDestination(name) {
        object Camera : TabTopLevelDestination("camera", R.string.camera_tab_title)
        object NewsFeed : TabTopLevelDestination("feed", R.string.feed_tab_title)
    }

    sealed class IconTopLevelDestination(
        name: String,
        val icon: Icon,
    ) : TopLevelDestination(name) {
        object Profile : IconTopLevelDestination("profile", ImageVectorIcon(MemoIcons.Profile))
        object Messenger : IconTopLevelDestination("messenger", ImageVectorIcon(MemoIcons.Send))

        object FriendsMap : IconTopLevelDestination("map", ImageVectorIcon(MemoIcons.Map))
        object Friends : IconTopLevelDestination("Friends", ImageVectorIcon(MemoIcons.Friends))

        object MemoriesGeneration :
            IconTopLevelDestination("memories_generation", ImageVectorIcon(MemoIcons.Memories))
    }
}

internal fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
