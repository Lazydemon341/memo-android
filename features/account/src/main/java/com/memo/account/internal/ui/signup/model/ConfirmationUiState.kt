package com.memo.account.internal.ui.signup.model

internal sealed interface ConfirmationUiState {
    object Success : ConfirmationUiState
    class Failure(val message: String?) : ConfirmationUiState
    object Loading : ConfirmationUiState
    object Input : ConfirmationUiState
}
