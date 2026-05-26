package com.example.rmp_frontend.presentation.state

sealed interface AuthUiState {
    data object Empty : AuthUiState

    data class Editing(
        val email: String = "",
        val password: String = "",
        val name: String = "",
        val isRegisterMode: Boolean = false,
        val emailError: String? = null,
        val passwordError: String? = null
    ) : AuthUiState

    data class Loading(
        val email: String,
        val password: String,
        val name: String,
        val isRegisterMode: Boolean
    ) : AuthUiState

    data class Error(
        val message: String,
        val email: String = "",
        val password: String = "",
        val name: String = "",
        val isRegisterMode: Boolean = false
    ) : AuthUiState

    data object Success : AuthUiState
}
