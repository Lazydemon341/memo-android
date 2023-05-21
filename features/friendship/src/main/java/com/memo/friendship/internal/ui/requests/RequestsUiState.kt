package com.memo.friendship.internal.ui.requests

import androidx.compose.runtime.Immutable
import com.memo.core.model.friendship.FriendshipRequest

@Immutable
internal sealed interface RequestsUiState {

    object Loading : RequestsUiState

    object Error : RequestsUiState

    @Immutable
    data class Ready(
        val requests: List<FriendshipRequest>
    ) : RequestsUiState
}
