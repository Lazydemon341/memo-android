package com.memo.core.data.common_models

import com.memo.core.datastore.friendship.Friend
import com.memo.core.datastore.friendship.FriendshipRequest
import com.memo.core.network.model.friendship.FriendResponse
import com.memo.core.network.model.friendship.FriendshipRequestResponse

fun FriendResponse.asLocalModel(): Friend {
    return Friend(
        id = id,
        name = name,
    )
}

fun FriendshipRequestResponse.asLocalModel(): FriendshipRequest {
    return FriendshipRequest(
        request_id = requestId,
        user_id = user.id,
        user_name = user.name,
    )
}
