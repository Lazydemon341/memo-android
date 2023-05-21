package com.memo.account.internal.ui.signup.model

internal sealed interface SignUpUiState {
    object Success : SignUpUiState
    class Failure(val message: String?) : SignUpUiState
    object Loading : SignUpUiState
    object Input : SignUpUiState
}
