package com.memo.core.data.repository

import com.memo.core.data.common_models.asLocalModel
import com.memo.core.datastore.friendship.FriendshipStoreDataSource
import com.memo.core.datastore.friendship.asExternalModel
import com.memo.core.model.friendship.Friend
import com.memo.core.model.friendship.FriendshipRequest
import com.memo.core.network.NetworkFriendshipDataSource
import com.memo.core.network.model.friendship.AcceptFriendshipRequestBody
import com.memo.core.network.model.friendship.DeclineFriendshipRequestBody
import com.memo.core.network.model.friendship.FriendResponse
import com.memo.core.network.model.friendship.FriendshipRequestResponse
import com.memo.core.network.model.friendship.RequestFriendshipBody
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FriendshipRepositoryImpl @Inject constructor(
    private val network: NetworkFriendshipDataSource,
    private val local: FriendshipStoreDataSource,
) : FriendshipRepository {

    override fun friendsFlow(): Flow<List<Friend>> {
        return local.friendsFlow()
            .map { friends ->
                friends.map { it.asExternalModel() }
            }
    }

    override fun requestsFlow(): Flow<List<FriendshipRequest>> {
        return local.requestsFlow()
            .map { requests ->
                requests.map { it.asExternalModel() }
            }
    }

    override suspend fun updateFriends(): Result<Unit> {
        return suspendRunCatching {
            val response = retryOnNetworkOrServerErrors {
                network.friends()
            }
            val friends = response.friends
                .map(FriendResponse::asLocalModel)
            local.updateFriends(friends)
        }
    }

    override suspend fun updateRequests(): Result<Unit> {
        return suspendRunCatching {
            val response = retryOnNetworkOrServerErrors {
                network.incomingRequests()
            }
            val requests = response.requests
                .map(FriendshipRequestResponse::asLocalModel)
            local.updateRequests(requests)
        }
    }

    override suspend fun sendRequest(userId: Long): Result<Unit> {
        return suspendRunCatching {
            val data = RequestFriendshipBody(
                userId = userId,
            )
            retryOnNetworkOrServerErrors {
                network.request(data)
            }
        }
    }

    override suspend fun acceptRequest(requestId: Long): Result<Unit> {
        return suspendRunCatching {
            val data = AcceptFriendshipRequestBody(
                requestId = requestId,
            )
            retryOnNetworkOrServerErrors {
                network.accept(data)
            }
        }
    }

    override suspend fun declineRequest(requestId: Long): Result<Unit> {
        return suspendRunCatching {
            val data = DeclineFriendshipRequestBody(
                requestId = requestId,
            )
            retryOnNetworkOrServerErrors {
                network.decline(data)
            }
        }
    }

    override suspend fun deleteFriend(friendId: Long): Result<Unit> {
        return suspendRunCatching {
            retryOnNetworkOrServerErrors {
                network.deleteFriend(friendId)
            }
        }
    }
}
