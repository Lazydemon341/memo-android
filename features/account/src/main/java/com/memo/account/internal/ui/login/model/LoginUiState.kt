package com.memo.account.internal.ui.login.model

internal sealed interface LoginUiState {
    object Success : LoginUiState
    class Failure(val message: String?) : LoginUiState
    object Loading : LoginUiState
    object Input : LoginUiState
}
