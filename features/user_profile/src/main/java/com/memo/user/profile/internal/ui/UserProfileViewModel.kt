package com.memo.user.profile.internal.ui

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.core.data.repository.UserRepository
import com.memo.user.profile.api.userProfileIDNavArgument
import com.memo.user.profile.internal.domain.ChangeAvatarUseCase
import com.memo.user.profile.internal.domain.GetProfileUseCase
import com.memo.user.profile.internal.ui.model.UserProfileUiState
import com.memo.user.profile.internal.ui.model.UserProfileUiStateMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class UserProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProfileUseCase: GetProfileUseCase,
    private val userProfileUiStateMapper: UserProfileUiStateMapper,
    private val changeAvatarUseCase: ChangeAvatarUseCase,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val userId: Long? =
        savedStateHandle.get<String?>(userProfileIDNavArgument)?.toLongOrNull()

    private val _userProfileState = MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    val userProfileState = _userProfileState.asStateFlow()

    fun avatarChanged(uri: Uri) {
        viewModelScope.launch {
            changeAvatarUseCase(uri)
            getUserProfileInner()
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            getUserProfileInner()
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    private suspend fun getUserProfileInner() {
        _userProfileState.value = UserProfileUiState.Loading
        _userProfileState.value = getProfileUseCase(userId = userId)
            .map {
                userProfileUiStateMapper.map(
                    profile = it,
                    isCurrentUser = userId == null,
                )
            }
            .getOrElse {
                Timber.e(it)
                UserProfileUiState.Error
            }
    }
}
