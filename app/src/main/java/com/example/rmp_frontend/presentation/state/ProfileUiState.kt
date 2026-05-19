package com.example.rmp_frontend.presentation.state

import com.example.rmp_frontend.domain.model.User

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val isAuthorized: Boolean = true,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
)
