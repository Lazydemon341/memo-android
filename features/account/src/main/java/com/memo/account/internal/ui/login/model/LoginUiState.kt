package com.memo.account.internal.ui.login.model

internal sealed interface LoginUiState {
    object Success : LoginUiState
    object Failure : LoginUiState
    object Loading : LoginUiState
    object Input : LoginUiState
}
