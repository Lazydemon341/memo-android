package com.memo.memories.internal.ui.model

import javax.annotation.concurrent.Immutable

internal sealed interface MemoryUiState {
    @Immutable
    data class Success(val memory: MemorySuccessUiState) : MemoryUiState
    object Error : MemoryUiState
    object Loading : MemoryUiState
}
