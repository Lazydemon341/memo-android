package com.memo.core.network

import com.memo.core.network.model.messaging.ChatResponse
import com.memo.core.network.model.messaging.ChatsResponse
import com.memo.core.network.model.messaging.MessagesPollingResponse
import com.memo.core.network.model.messaging.SendMessageParams

interface NetworkMessagingDataSource {
    suspend fun getChats(): ChatsResponse

    suspend fun getChat(
        chatId: Long,
        countMessages: Int,
        startMessageId: Long?,
    ): ChatResponse

    suspend fun sendMessage(sendMessageParams: SendMessageParams)

    suspend fun pollMessages(): MessagesPollingResponse
}
