package com.memo.chats.list.api.data

import com.memo.chats.list.api.domain.Chat
import com.memo.chats.list.api.domain.ChatsListRepository
import com.memo.chats.list.internal.data.toDomain
import com.memo.core.network.NetworkMessagingDataSource
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import timber.log.Timber
import javax.inject.Inject

/* TODO: getChats() should return Flow and fetch data from local storage.
 * add updateChats() which will fetch data from network and update local data.
 */

class ChatsListRepositoryImpl @Inject constructor(
    private val network: NetworkMessagingDataSource,
) : ChatsListRepository {
    override suspend fun getChats(): Result<List<Chat>> {
        return suspendRunCatching {
            val response = retryOnNetworkOrServerErrors {
                network.getChats()
            }
            response.chats.map { it.toDomain() }
        }.onFailure {
            Timber.e(it, "Cannot load chats")
        }
    }
}
