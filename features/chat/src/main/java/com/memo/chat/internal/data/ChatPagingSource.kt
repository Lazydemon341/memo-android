package com.memo.chat.internal.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.memo.core.data.common_models.toDomainModel
import com.memo.core.model.messaging.Message
import com.memo.core.network.NetworkMessagingDataSource
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.trySuspend
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val CHAT_ID = "chat_id"

class ChatPagingSource @AssistedInject constructor(
    private val networkDataSource: NetworkMessagingDataSource,
    @Assisted(CHAT_ID) private val chatId: Long,
) : PagingSource<Long, Message>() {

    override fun getRefreshKey(state: PagingState<Long, Message>): Long? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Message> {
        return trySuspend(
            run = {
                val messageId = params.key
                val response = retryOnNetworkOrServerErrors {
                    networkDataSource.getChat(
                        chatId = chatId,
                        countMessages = params.loadSize,
                        startMessageId = messageId,
                    )
                }
                val data = response.messages.map { it.toDomainModel() }
                LoadResult.Page(
                    data = data,
                    prevKey = null,
                    nextKey = data.lastOrNull()?.id,
                )
            },
            catch = {
                LoadResult.Error(it)
            }
        )
    }
}

@AssistedFactory
interface ChatPagingSourceFactory {
    fun create(
        @Assisted(CHAT_ID) chatId: Long,
    ): ChatPagingSource
}
