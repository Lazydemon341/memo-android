package com.memo.friendship.internal.utils

private const val FRIENDSHIP_ADD_DEEPLINK = "memoapp://friendship/add"
private const val FRIENDSHIP_ADD_ID_PARAM = "id"

internal object FriendshipDeeplinks {

    fun getAddFriendDeeplink(userId: Long): String {
        return "$FRIENDSHIP_ADD_DEEPLINK?$FRIENDSHIP_ADD_ID_PARAM=$userId"
    }
}
