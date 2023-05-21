package com.memo.user.profile.internal.domain.model

internal class UserProfile(
    val avatarUrl: String?,
    val name: String,
    val email: String,
    val friendsCount: Int,
    val memories: List<UserProfileMemory>,
)
