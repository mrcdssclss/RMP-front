package com.example.rmp_frontend.presentation.state

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data object Empty : ProfileUiState
    data class Error(val message: String) : ProfileUiState

    data class Success(
        val user: UserProfileUiModel,
        val appVersion: String
    ) : ProfileUiState
}
