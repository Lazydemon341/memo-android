package com.memo.chats.list.internal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.chats.list.internal.domain.GetChatsUseCase
import com.memo.chats.list.internal.ui.model.ChatUiState
import com.memo.chats.list.internal.ui.model.ChatUiStateMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ChatsListViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val chatUiStateMapper: ChatUiStateMapper,
) : ViewModel() {

    private val _chatsUiState = MutableStateFlow<List<ChatUiState>>(emptyList())
    val chatsUiState = _chatsUiState.asStateFlow()

    private val _loadingState = MutableStateFlow(true)
    val loadingState = _loadingState.asStateFlow()

    fun updateChats() = viewModelScope.launch {
        _loadingState.value = true
        _chatsUiState.value = getChatsUseCase()
            .map { chatUiStateMapper.map(it) }
            .getOrDefault(emptyList())
        _loadingState.value = false
    }
}
