package com.memo.chat.internal.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.memo.chat.api.navigation.chatIdNavArgument
import com.memo.chat.api.navigation.chatInterlocutorEmailNavArgument
import com.memo.chat.api.navigation.chatTitleNavArgument
import com.memo.chat.internal.data.ChatPagingSourceFactory
import com.memo.chat.internal.domain.SendMessageUseCase
import com.memo.chat.internal.domain.model.MessageToSend
import com.memo.chat.internal.ui.model.MessageUiState
import com.memo.chat.internal.ui.model.MessageUiStateMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PAGE_SIZE = 30

@HiltViewModel
internal class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatPagingSourceFactory: ChatPagingSourceFactory,
    private val messageUiStateMapper: MessageUiStateMapper,
    private val sendMessageUseCase: SendMessageUseCase,
) : ViewModel() {

    private val chatId: Long = checkNotNull(savedStateHandle[chatIdNavArgument])
    private val interlocutorEmail: String =
        checkNotNull(savedStateHandle[chatInterlocutorEmailNavArgument])

    val title: String = checkNotNull(savedStateHandle[chatTitleNavArgument])

    val messagesPagingFlow = messagesPagingFlow()
        .cachedIn(viewModelScope)

    suspend fun sendMessage(text: String) {
        val message = MessageToSend(
            text = text,
            chatId = chatId,
        )
        sendMessageUseCase(message = message)
    }

    private fun messagesPagingFlow(): Flow<PagingData<MessageUiState>> {
        val pager = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { chatPagingSourceFactory.create(chatId) },
        )
        return pager.flow
            .map { pagingData ->
                pagingData.map {
                    messageUiStateMapper.mapMessage(
                        message = it,
                        interlocutorEmail = interlocutorEmail,
                    )
                }
            }
    }
}
