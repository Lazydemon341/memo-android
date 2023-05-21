package com.memo.core.network.retrofit

import com.memo.core.network.NetworkMessagingDataSource
import com.memo.core.network.model.messaging.ChatResponse
import com.memo.core.network.model.messaging.ChatsResponse
import com.memo.core.network.model.messaging.MessagesPollingResponse
import com.memo.core.network.model.messaging.SendMessageParams
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface MessagingApi {

    @GET("messaging/chats")
    suspend fun getChats(): ChatsResponse

    @GET("messaging/chat")
    suspend fun getChat(
        @Query("chat_id") chatId: Long,
        @Query("count_messages") countMessages: Int,
        @Query("start_message_id") startMessageId: Long?,
    ): ChatResponse

    @POST("messaging/send")
    suspend fun send(@Body sendMessageParams: SendMessageParams)

    @GET("messaging/lp")
    suspend fun pollMessages(): MessagesPollingResponse
}

@Singleton
class RetrofitMessagingDataSource @Inject constructor(
    apiFactory: ApiFactory,
) : NetworkMessagingDataSource {

    private val api = apiFactory.create(MessagingApi::class.java)

    override suspend fun getChats(): ChatsResponse {
        return api.getChats()
    }

    override suspend fun getChat(
        chatId: Long,
        countMessages: Int,
        startMessageId: Long?,
    ): ChatResponse {
        return api.getChat(
            chatId = chatId,
            countMessages = countMessages,
            startMessageId = startMessageId,
        )
    }

    override suspend fun sendMessage(sendMessageParams: SendMessageParams) {
        return api.send(sendMessageParams = sendMessageParams)
    }

    override suspend fun pollMessages(): MessagesPollingResponse {
        return api.pollMessages()
    }
}
