package com.memo.publish_photo.internal.ui

import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.publish_photo.internal.domain.GetFriendsUseCase
import com.memo.publish_photo.internal.domain.GetLocationUseCase
import com.memo.publish_photo.internal.domain.GetPhotoUseCase
import com.memo.publish_photo.internal.domain.PublishPhotoUseCase
import com.memo.publish_photo.internal.model.FriendUiState
import com.memo.publish_photo.internal.model.LocationUiState
import com.memo.publish_photo.internal.model.PhotoUploadingUiState
import com.memo.publish_photo.internal.model.PublishPhotoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class PublishPhotoViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val publishPhotoUseCase: PublishPhotoUseCase,
    private val getPhotoUseCase: GetPhotoUseCase,
    private val getLocationUseCase: GetLocationUseCase,
) : ViewModel() {

    private val selectedFriendsIdsState = MutableStateFlow<List<Long>>(emptyList())

    private val _uploadingUiState =
        MutableStateFlow<PhotoUploadingUiState>(PhotoUploadingUiState.None)
    val uploadingUiState: StateFlow<PhotoUploadingUiState> = _uploadingUiState

    val publishPhotoUiState = publishPhotoUiStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PublishPhotoUiState.Loading,
        )

    fun onFriendClick(id: Long, select: Boolean) {
        selectedFriendsIdsState.update {
            it.toMutableList().apply {
                if (select) {
                    add(id)
                } else {
                    remove(id)
                }
            }
        }
    }

    fun publishPhoto() {
        viewModelScope.launch {
            _uploadingUiState.value = PhotoUploadingUiState.Loading
            val result = publishPhotoUseCase()
            _uploadingUiState.value = result.map {
                PhotoUploadingUiState.Success
            }.getOrElse {
                PhotoUploadingUiState.Failure(it.message)
            }
        }
    }

    fun downloadPhoto() {
        // TODO
    }

    private fun publishPhotoUiStateFlow(): Flow<PublishPhotoUiState> {
        return combine(
            friendsUiStateFlow(),
            flow { emit(getPhotoUseCase()) },
            locationUiStateFlow(),
        ) { friends, photo, location ->
            PublishPhotoUiState.Ready(
                photo = photo.getOrThrow().asImageBitmap(),
                friends = friends,
                locationUiState = location,
            ) as PublishPhotoUiState
        }.onStart {
            emit(PublishPhotoUiState.Loading)
        }.catch {
            Timber.e(it, "Error getting PublishPhotoUiState")
            emit(PublishPhotoUiState.Error)
        }.flowOn(Dispatchers.Default)
    }

    private fun friendsUiStateFlow(): Flow<List<FriendUiState>> {
        return combine(
            getFriendsUseCase(),
            selectedFriendsIdsState,
        ) { friends, selected ->
            friends.map { friend ->
                FriendUiState(
                    id = friend.id,
                    imageUrl = "",
                    name = friend.name.split(" ").firstOrNull().orEmpty(),
                    isSelected = selected.contains(friend.id),
                )
            }
        }.onStart { emit(emptyList()) }
    }

    private fun locationUiStateFlow(): Flow<LocationUiState> {
        return getLocationUseCase().map { location ->
            LocationUiState.Enabled(
                location = location?.getAddressLine(0).orEmpty(),
                enabled = true,
            ) as LocationUiState
        }.catch {
            Timber.e(it, "error getting location")
            emit(LocationUiState.Empty)
        }.onStart {
            emit(LocationUiState.Empty)
        }
    }
}
