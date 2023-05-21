package com.memo.memories.upload.internal.model

import androidx.annotation.StringRes

internal sealed interface MemoryLoadingUiState {
    object None : MemoryLoadingUiState
    object Loading : MemoryLoadingUiState
    class Error(@StringRes val errorResId: Int) : MemoryLoadingUiState
    object Success : MemoryLoadingUiState
}
