package com.example.rmp_frontend.model

sealed class HomeUiState {

    object Loading : HomeUiState()

    data class Error(
        val message: String
    ) : HomeUiState()

    data class Success(
        val assets: List<Asset>,
        val totalBalance: Double
    ) : HomeUiState()
}