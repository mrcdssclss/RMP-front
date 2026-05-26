package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.repository.AuthRepository
import com.example.rmp_frontend.presentation.state.SplashUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching { authRepository.currentToken() }
                .onSuccess {
                    _uiState.value = if (it == null) SplashUiState.NavigateToAuth else SplashUiState.NavigateToMain
                }
                .onFailure { _uiState.value = SplashUiState.Error(it.toUserMessage()) }
        }
    }
}
