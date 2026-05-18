package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.presentation.state.AuthUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Empty)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = AuthUiState.Editing()
    }

    fun showLogin() {
        val form = currentForm()
        _uiState.value = form.copy(isRegisterMode = false, emailError = null, passwordError = null)
    }

    fun showRegister() {
        val form = currentForm()
        _uiState.value = form.copy(isRegisterMode = true, emailError = null, passwordError = null)
    }

    fun onEmailChange(value: String) {
        val form = currentForm()
        _uiState.value = form.copy(email = value, emailError = null)
    }

    fun onPasswordChange(value: String) {
        val form = currentForm()
        _uiState.value = form.copy(password = value, passwordError = null)
    }

    fun onNameChange(value: String) {
        val form = currentForm()
        _uiState.value = form.copy(name = value)
    }

    fun onLoginClick() {
        submit(isRegisterMode = false)
    }

    fun onRegisterClick() {
        submit(isRegisterMode = true)
    }

    fun clearAuthResult() {
        _uiState.value = currentForm()
    }

    private fun submit(isRegisterMode: Boolean) {
        val form = currentForm().copy(isRegisterMode = isRegisterMode)
        val emailError = if (form.email.isBlank()) "Email is required" else null
        val passwordError = if (form.password.length < 4) "Password is too short" else null

        if (emailError != null || passwordError != null) {
            _uiState.value = form.copy(emailError = emailError, passwordError = passwordError)
            return
        }

        _uiState.value = AuthUiState.Loading(
            email = form.email,
            password = form.password,
            name = form.name,
            isRegisterMode = isRegisterMode
        )

        viewModelScope.launch {
            delay(500)
            _uiState.value = AuthUiState.Success
        }
    }

    private fun currentForm(): AuthUiState.Editing {
        return when (val state = _uiState.value) {
            AuthUiState.Empty -> AuthUiState.Editing()
            is AuthUiState.Editing -> state
            is AuthUiState.Error -> AuthUiState.Editing(
                email = state.email,
                password = state.password,
                name = state.name,
                isRegisterMode = state.isRegisterMode
            )
            is AuthUiState.Loading -> AuthUiState.Editing(
                email = state.email,
                password = state.password,
                name = state.name,
                isRegisterMode = state.isRegisterMode
            )
            AuthUiState.Success -> AuthUiState.Editing()
        }
    }
}
