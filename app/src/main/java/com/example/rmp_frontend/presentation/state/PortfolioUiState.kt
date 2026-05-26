package com.example.rmp_frontend.presentation.state

sealed interface PortfolioUiState {
    data object Loading : PortfolioUiState
    data object Empty : PortfolioUiState
    data class Error(val message: String) : PortfolioUiState

    data class Success(
        val cashBalance: Double,
        val totalValue: Double,
        val assets: List<PortfolioAssetUiModel>,
    ) : PortfolioUiState
}
