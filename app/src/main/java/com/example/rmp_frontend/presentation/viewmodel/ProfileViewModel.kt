package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.repository.AuthRepository
import com.example.rmp_frontend.domain.repository.ProfileRepository
import com.example.rmp_frontend.presentation.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { profileRepository.getProfile() }
                .onSuccess { user ->
                    _uiState.value = ProfileUiState(user = user, isEmpty = false)
                }
                .onFailure { error -> _uiState.value = ProfileUiState(errorMessage = error.toUserMessage()) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = ProfileUiState(isAuthorized = false, isEmpty = true)
        }
    }
}
