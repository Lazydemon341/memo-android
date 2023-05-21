package com.memo.friendship.internal.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.core.data.repository.FriendshipRepository
import com.memo.friendship.internal.domain.GetRequestQRUseCase
import com.memo.friendship.internal.ui.friends.FriendsUiState
import com.memo.friendship.internal.ui.requests.RequestsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class FriendshipViewModel @Inject constructor(
    private val friendshipRepository: FriendshipRepository,
    private val getRequestQRUseCase: GetRequestQRUseCase,
) : ViewModel() {

    private val _updatingState = MutableStateFlow(false)
    val updatingState = _updatingState.asStateFlow()

    val friendsUiState = friendsUiStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FriendsUiState.Loading,
        )

    val requestsUiState = requestsUiStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RequestsUiState.Loading,
        )

    val friendshipQRState = friendshipQRFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun updateFriendsAndRequests() {
        viewModelScope.launch {
            _updatingState.value = true
            val friends = async { friendshipRepository.updateFriends() }
            val requests = async { friendshipRepository.updateRequests() }
            friends.await()
            requests.await()
            _updatingState.value = false
        }
    }

    fun acceptRequest(requestId: Long) {
        viewModelScope.launch {
            friendshipRepository.acceptRequest(requestId)
            updateFriendsAndRequests()
        }
    }

    fun declineRequest(requestId: Long) {
        viewModelScope.launch {
            friendshipRepository.declineRequest(requestId)
            updateFriendsAndRequests()
        }
    }

    fun deleteFriend(friendId: Long) {
        viewModelScope.launch {
            friendshipRepository.deleteFriend(friendId)
            updateFriendsAndRequests()
        }
    }

    private fun friendshipQRFlow(): Flow<Bitmap?> {
        return flow { emit(getRequestQRUseCase().getOrNull()) }
            .retry(retries = 5L) {
                Timber.e(it, "Failed to generate friendship QR")
                delay(500L)
                true
            }
    }

    private fun friendsUiStateFlow(): Flow<FriendsUiState> {
        return friendshipRepository.friendsFlow()
            .map { friends ->
                FriendsUiState.Ready(
                    friends = friends,
                ) as FriendsUiState
            }.onStart { emit(FriendsUiState.Loading) }
            .catch { emit(FriendsUiState.Error) }
    }

    private fun requestsUiStateFlow(): Flow<RequestsUiState> {
        return friendshipRepository.requestsFlow()
            .map { requests ->
                RequestsUiState.Ready(
                    requests = requests,
                ) as RequestsUiState
            }.onStart { emit(RequestsUiState.Loading) }
            .catch { emit(RequestsUiState.Error) }
    }
}
