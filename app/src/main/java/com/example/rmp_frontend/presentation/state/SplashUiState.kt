package com.example.rmp_frontend.presentation.state

sealed interface SplashUiState {
    data object Loading : SplashUiState
    data object NavigateToAuth : SplashUiState
    data object NavigateToMain : SplashUiState
    data class Error(val message: String) : SplashUiState
}
