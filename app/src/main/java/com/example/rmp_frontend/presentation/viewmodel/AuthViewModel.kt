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
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Editing())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun showLogin() {
        _uiState.value = currentForm().copy(isRegisterMode = false, emailError = null, passwordError = null)
    }

    fun showRegister() {
        _uiState.value = currentForm().copy(isRegisterMode = true, emailError = null, passwordError = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = currentForm().copy(email = value, emailError = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = currentForm().copy(password = value, passwordError = null)
    }

    fun onNameChange(value: String) {
        _uiState.value = currentForm().copy(name = value)
    }

    fun onLoginClick() = submit(isRegisterMode = false)

    fun onRegisterClick() = submit(isRegisterMode = true)

    fun clearAuthResult() {
        _uiState.value = AuthUiState.Editing()
    }

    private fun submit(isRegisterMode: Boolean) {
        val form = currentForm().copy(isRegisterMode = isRegisterMode)
        val emailError = if (!form.email.contains('@')) "Enter a valid email" else null
        val passwordError = when {
            form.password.isBlank() -> "Password is required"
            isRegisterMode && form.password.length < 8 -> "Password must contain at least 8 characters"
            else -> null
        }
        if (emailError != null || passwordError != null) {
            _uiState.value = form.copy(emailError = emailError, passwordError = passwordError)
            return
        }

        _uiState.value = AuthUiState.Loading(form.email, form.password, form.name, isRegisterMode)
        viewModelScope.launch {
            runCatching {
                if (isRegisterMode) {
                    authRepository.register(form.email, form.password, form.name.trim().takeIf(String::isNotEmpty))
                } else {
                    authRepository.login(form.email, form.password)
                }
            }.onSuccess {
                _uiState.value = AuthUiState.Success
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(
                    message = error.toUserMessage(),
                    email = form.email,
                    password = form.password,
                    name = form.name,
                    isRegisterMode = isRegisterMode,
                )
            }
        }
    }

    private fun currentForm(): AuthUiState.Editing = when (val state = _uiState.value) {
        AuthUiState.Empty, AuthUiState.Success -> AuthUiState.Editing()
        is AuthUiState.Editing -> state
        is AuthUiState.Error -> AuthUiState.Editing(state.email, state.password, state.name, state.isRegisterMode)
        is AuthUiState.Loading -> AuthUiState.Editing(state.email, state.password, state.name, state.isRegisterMode)
    }
}
