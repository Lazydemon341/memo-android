package com.memo.core.network.model.friendship

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FriendsResponse(
    @Json(name = "friends")
    val friends: List<FriendResponse>,
)
