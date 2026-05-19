package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rmp_frontend.presentation.state.ProfileUiState
import com.example.rmp_frontend.presentation.state.UserProfileUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun onRefreshClick() {
        loadProfile()
    }

    fun onUpdateCredentials(login: String, password: String) {
        val state = _uiState.value as? ProfileUiState.Success ?: return
        val trimmedLogin = login.trim()

        if (trimmedLogin.isBlank()) {
            _uiState.value = state.copy(
                credentialsMessage = null,
                credentialsError = "Login is required",
                credentialsEventId = state.credentialsEventId + 1
            )
            return
        }

        if (password.length < 4) {
            _uiState.value = state.copy(
                credentialsMessage = null,
                credentialsError = "Password is too short",
                credentialsEventId = state.credentialsEventId + 1
            )
            return
        }

        // UI stub only: password is not persisted here. Developer 2 should wire secure credentials update.
        _uiState.value = state.copy(
            user = state.user.copy(email = trimmedLogin),
            credentialsMessage = "Данные профиля обновлены",
            credentialsError = null,
            credentialsEventId = state.credentialsEventId + 1
        )
    }

    fun onLogoutClick() {
        _uiState.value = ProfileUiState.Success(
            user = UserProfileUiModel(
                name = "Guest",
                email = "not authorized",
                isAuthorized = false
            ),
            appVersion = "1.0"
        )
    }

    private fun loadProfile() {
        _uiState.value = ProfileUiState.Success(
            user = UserProfileUiModel(
                name = "Demo Trader",
                email = "demo.trader@example.com",
                isAuthorized = true
            ),
            appVersion = "1.0"
        )
    }
}
