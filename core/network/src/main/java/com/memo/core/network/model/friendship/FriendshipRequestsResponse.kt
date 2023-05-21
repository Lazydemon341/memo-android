package com.memo.core.network.model.friendship

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FriendshipRequestsResponse(
    @Json(name = "requests")
    val requests: List<FriendshipRequestResponse>,
)

@JsonClass(generateAdapter = true)
class FriendshipRequestResponse(
    @Json(name = "request_id")
    val requestId: Long,

    @Json(name = "user")
    val user: User,
) {

    @JsonClass(generateAdapter = true)
    class User(
        @Json(name = "id")
        val id: Long,

        @Json(name = "name")
        val name: String,
    )
}
