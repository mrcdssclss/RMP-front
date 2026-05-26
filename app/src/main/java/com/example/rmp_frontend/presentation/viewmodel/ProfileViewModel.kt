package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.repository.AuthRepository
import com.example.rmp_frontend.domain.repository.ProfileRepository
import com.example.rmp_frontend.presentation.state.ProfileUiState
import com.example.rmp_frontend.presentation.state.UserProfileUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun onRefreshClick() = loadProfile()

    fun onUpdateProfile(name: String) {
        val state = _uiState.value as? ProfileUiState.Success ?: return
        if (name.isBlank()) {
            _uiState.value = state.copy(
                credentialsMessage = null,
                credentialsError = "Name is required",
                credentialsEventId = state.credentialsEventId + 1,
            )
            return
        }
        viewModelScope.launch {
            runCatching { profileRepository.updateProfile(name.trim(), null) }
                .onSuccess { user ->
                    _uiState.value = state.copy(
                        user = UserProfileUiModel(user.displayName, user.email ?: user.login, true),
                        credentialsMessage = "Profile updated",
                        credentialsError = null,
                        credentialsEventId = state.credentialsEventId + 1,
                    )
                }
                .onFailure { error ->
                    _uiState.value = state.copy(
                        credentialsMessage = null,
                        credentialsError = error.toUserMessage(),
                        credentialsEventId = state.credentialsEventId + 1,
                    )
                }
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = ProfileUiState.Success(
                UserProfileUiModel("Guest", "not authorized", false),
                appVersion = "1.0",
            )
        }
    }

    private fun loadProfile() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            runCatching { profileRepository.getProfile() }
                .onSuccess { user ->
                    _uiState.value = ProfileUiState.Success(
                        user = UserProfileUiModel(user.displayName, user.email ?: user.login, true),
                        appVersion = "1.0",
                    )
                }
                .onFailure { _uiState.value = ProfileUiState.Error(it.toUserMessage()) }
        }
    }
}
