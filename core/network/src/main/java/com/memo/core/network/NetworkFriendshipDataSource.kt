package com.memo.core.network

import com.memo.core.network.model.friendship.AcceptFriendshipRequestBody
import com.memo.core.network.model.friendship.DeclineFriendshipRequestBody
import com.memo.core.network.model.friendship.FriendsResponse
import com.memo.core.network.model.friendship.FriendshipRequestsResponse
import com.memo.core.network.model.friendship.RequestFriendshipBody

interface NetworkFriendshipDataSource {

    suspend fun request(data: RequestFriendshipBody)

    suspend fun incomingRequests(): FriendshipRequestsResponse

    suspend fun friends(): FriendsResponse

    suspend fun decline(data: DeclineFriendshipRequestBody)

    suspend fun accept(data: AcceptFriendshipRequestBody)

    suspend fun deleteFriend(friendId: Long)
}
