package com.memo.memories.generation.internal.list

import android.net.Uri
import com.memo.core.model.memories.GeneratedMemory

internal data class MemoryPreviewUiState(
    val memoryId: Long,
    val title: String,
    val caption: String,
    val previewPhotoUri: Uri,
)

internal fun GeneratedMemory.toMemoryPreviewUiState(): MemoryPreviewUiState {
    return MemoryPreviewUiState(
        memoryId = memoryId,
        title = title,
        caption = caption,
        previewPhotoUri = Uri.parse(photos[0].photoUri),
    )
}
