package com.memo.friendship.internal.ui.friends

import androidx.compose.runtime.Immutable
import com.memo.core.model.friendship.Friend

@Immutable
internal sealed interface FriendsUiState {

    object Loading : FriendsUiState

    object Error : FriendsUiState

    @Immutable
    data class Ready(
        val friends: List<Friend>
    ) : FriendsUiState
}
