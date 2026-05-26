package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.repository.AuthRepository
import com.example.rmp_frontend.presentation.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun checkSession() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { authRepository.currentToken() }
                .onSuccess { token ->
                    _uiState.value = AuthUiState(isAuthorized = token != null, isEmpty = token == null)
                }
                .onFailure { error ->
                    _uiState.value = AuthUiState(errorMessage = error.toUserMessage())
                }
        }
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { authRepository.login(login, password) }
                .onSuccess { _uiState.value = AuthUiState(isAuthorized = true) }
                .onFailure { error -> _uiState.value = AuthUiState(errorMessage = error.toUserMessage()) }
        }
    }

    fun register(login: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { authRepository.register(login, password) }
                .onSuccess { _uiState.value = AuthUiState(isAuthorized = true) }
                .onFailure { error -> _uiState.value = AuthUiState(errorMessage = error.toUserMessage()) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState(isAuthorized = false, isEmpty = true)
        }
    }
}
