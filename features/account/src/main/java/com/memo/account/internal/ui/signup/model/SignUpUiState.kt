package com.memo.account.internal.ui.signup.model

internal sealed interface SignUpUiState {
    object Success : SignUpUiState
    object Failure : SignUpUiState
    object Loading : SignUpUiState
    object Input : SignUpUiState
}
