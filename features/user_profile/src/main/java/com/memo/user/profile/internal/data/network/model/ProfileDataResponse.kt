package com.memo.user.profile.internal.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ProfileDataResponse(
    @Json(name = "avatar_url")
    val avatarUrl: String?,

    @Json(name = "name")
    val name: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "friends_count")
    val friendsCount: Int,

    @Json(name = "memories")
    val memories: List<ProfileMemoryDTO>,
)
