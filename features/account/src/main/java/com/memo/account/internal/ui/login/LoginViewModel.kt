package com.memo.account.internal.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.account.internal.ui.login.model.LoginUiState
import com.memo.core.data.repository.UserRepository
import com.memo.core.model.user.AuthParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _loginUiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState.Input)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun onLogin(authParams: AuthParams) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            val result = userRepository.auth(authParams)
            _loginUiState.value = result.map {
                LoginUiState.Success
            }.getOrElse {
                LoginUiState.Failure(it.message)
            }
        }
    }
}
