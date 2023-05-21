package com.memo.core.model.friendship

data class FriendshipRequest(
    val id: Long,
    val user: FriendshipRequestUser,
)

data class FriendshipRequestUser(
    val id: Long,
    val name: String,
)
