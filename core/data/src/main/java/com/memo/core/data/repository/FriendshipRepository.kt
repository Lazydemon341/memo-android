package com.memo.core.data.repository

import com.memo.core.model.friendship.Friend
import com.memo.core.model.friendship.FriendshipRequest
import kotlinx.coroutines.flow.Flow

interface FriendshipRepository {

    fun friendsFlow(): Flow<List<Friend>>

    fun requestsFlow(): Flow<List<FriendshipRequest>>

    suspend fun updateFriends(): Result<Unit>

    suspend fun updateRequests(): Result<Unit>

    suspend fun sendRequest(userId: Long): Result<Unit>

    suspend fun acceptRequest(requestId: Long): Result<Unit>

    suspend fun declineRequest(requestId: Long): Result<Unit>

    suspend fun deleteFriend(friendId: Long): Result<Unit>
}
