package com.memo.chat.internal.data

import com.memo.chat.internal.domain.model.MessageToSend
import com.memo.core.data.common_models.toDomainModel
import com.memo.core.model.messaging.Message
import com.memo.core.network.NetworkMessagingDataSource
import com.memo.core.network.model.messaging.MessagesPollingResponse
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import com.memo.core.utils.trySuspend
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import timber.log.Timber
import javax.inject.Inject

private const val ERROR_RETRY_INTERVAL = 2000L

internal class ChatRepository @Inject constructor(
    private val networkMessagingDataSource: NetworkMessagingDataSource,
) {
    suspend fun sendMessage(message: MessageToSend): Result<Unit> {
        return suspendRunCatching {
            retryOnNetworkOrServerErrors {
                networkMessagingDataSource.sendMessage(
                    sendMessageParams = message.toNetworkModel(),
                )
            }
        }.onFailure {
            Timber.e(it, "Error sending massage")
        }
    }

    fun pollMessages(): Flow<List<Message>> {
        // TODO: insert into database, merge with polling from RemoteMediator
        return pollingFlow()
            .map { response ->
                response.messages.map { it.toDomainModel() }
            }
    }

    private fun pollingFlow(): Flow<MessagesPollingResponse> = flow {
        while (currentCoroutineContext().isActive) {
            trySuspend(
                run = {
                    emit(networkMessagingDataSource.pollMessages())
                },
                catch = {
                    Timber.e(it, "Messages polling error")
                    delay(ERROR_RETRY_INTERVAL)
                }
            )
        }
    }
}
