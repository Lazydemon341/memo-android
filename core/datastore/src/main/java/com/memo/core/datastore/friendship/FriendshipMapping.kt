package com.memo.core.datastore.friendship

import com.memo.core.model.friendship.Friend
import com.memo.core.model.friendship.FriendshipRequest
import com.memo.core.model.friendship.FriendshipRequestUser
import com.memo.core.datastore.friendship.Friend as PersistedFriend
import com.memo.core.datastore.friendship.FriendshipRequest as PersistedFriendshipRequest

fun PersistedFriend.asExternalModel(): Friend {
    return Friend(
        id = id,
        name = name,
    )
}

fun PersistedFriendshipRequest.asExternalModel(): FriendshipRequest {
    return FriendshipRequest(
        id = request_id,
        user = FriendshipRequestUser(
            id = user_id,
            name = user_name,
        ),
    )
}
