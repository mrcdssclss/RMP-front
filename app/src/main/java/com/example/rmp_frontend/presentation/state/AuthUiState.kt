package com.example.rmp_frontend.presentation.state

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthorized: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
)
