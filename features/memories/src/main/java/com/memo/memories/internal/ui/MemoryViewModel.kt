package com.memo.memories.internal.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.memories.api.navigation.memoryIdNavArgument
import com.memo.memories.internal.data.MemoryRepository
import com.memo.memories.internal.ui.model.MemoryUiState
import com.memo.memories.internal.ui.model.MemoryUiStateMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class MemoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val memoryRepository: MemoryRepository,
    private val memoryUiStateMapper: MemoryUiStateMapper,
) : ViewModel() {

    private val memoryId: Long = checkNotNull(savedStateHandle[memoryIdNavArgument])

    private val _memoryUiState = MutableStateFlow<MemoryUiState>(MemoryUiState.Loading)
    val memoryUiState: StateFlow<MemoryUiState> = _memoryUiState

    fun loadMemory() = viewModelScope.launch {
        _memoryUiState.value = MemoryUiState.Loading
        _memoryUiState.value = memoryRepository.getMemory(memoryId = memoryId)
            .mapCatching { MemoryUiState.Success(memoryUiStateMapper.map(it)) }
            .getOrElse { e ->
                Timber.e(e, "Failed to fetch memory")
                MemoryUiState.Error
            }
    }

    fun onPostLikeClick(isLiked: Boolean) {
        // TODO
    }

    fun onPostCommentsClick() {
        // TODO
    }

    fun onGalleryLikeClick(isLiked: Boolean) {
        // TODO
    }
}
