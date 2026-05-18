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
