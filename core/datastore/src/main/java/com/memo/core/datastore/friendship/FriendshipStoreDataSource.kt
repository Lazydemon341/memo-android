package com.memo.core.datastore.friendship

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class FriendshipStoreDataSource @Inject constructor(
    private val dataStore: DataStore<Friendship>,
) {

    fun friendsFlow(): Flow<List<Friend>> {
        return dataStore.data
            .map { it.friends }
    }

    fun requestsFlow(): Flow<List<FriendshipRequest>> {
        return dataStore.data
            .map { it.requests }
    }

    @Throws(IOException::class)
    suspend fun updateFriends(friends: List<Friend>) {
        dataStore.updateData {
            it.copy(friends = friends)
        }
    }

    @Throws(IOException::class)
    suspend fun updateRequests(requests: List<FriendshipRequest>) {
        dataStore.updateData {
            it.copy(requests = requests)
        }
    }
}
