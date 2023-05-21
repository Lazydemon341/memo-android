package com.memo.memories.generation.internal.list

import androidx.compose.runtime.Stable

internal sealed interface MemoriesListUiState {
    object Empty : MemoriesListUiState
    @Stable
    class NotEmpty(val memories: List<MemoryPreviewUiState>) : MemoriesListUiState
}
