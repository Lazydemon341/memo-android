package com.memo.account.internal.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.account.internal.ui.signup.model.ConfirmationUiState
import com.memo.account.internal.ui.signup.model.SignUpUiState
import com.memo.core.data.repository.UserRepository
import com.memo.core.model.user.SignUpParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _signUpUiState: MutableStateFlow<SignUpUiState> =
        MutableStateFlow(SignUpUiState.Input)
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState

    private val _confirmationUiState: MutableStateFlow<ConfirmationUiState> =
        MutableStateFlow(ConfirmationUiState.Input)
    val confirmationUiState: StateFlow<ConfirmationUiState> = _confirmationUiState

    fun signUp(signUpParams: SignUpParams) {
        viewModelScope.launch {
            _signUpUiState.value = SignUpUiState.Loading

            val registrationResult = userRepository.register(signUpParams)

            _signUpUiState.value = registrationResult.map {
                SignUpUiState.Success
            }.getOrElse { e ->
                Timber.e(e)
                SignUpUiState.Failure(e.message)
            }
        }
    }

    fun confirm(confirmationCode: Int) {
        viewModelScope.launch {
            _confirmationUiState.value = ConfirmationUiState.Loading
            val confirmationResult = userRepository.confirmRegistration(confirmationCode)
            _confirmationUiState.value = confirmationResult.map {
                ConfirmationUiState.Success
            }.getOrElse { e ->
                Timber.e(e)
                ConfirmationUiState.Failure(e.message)
            }
        }
    }
}
