package com.memo.account.internal.ui.signup.model

internal sealed interface ConfirmationUiState {
    object Success : ConfirmationUiState
    object Failure : ConfirmationUiState
    object Loading : ConfirmationUiState
    object Input : ConfirmationUiState
}
