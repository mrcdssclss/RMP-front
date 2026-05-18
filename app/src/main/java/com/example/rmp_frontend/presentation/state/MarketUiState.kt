package com.example.rmp_frontend.presentation.state

sealed interface MarketUiState {
    data object Loading : MarketUiState
    data object Empty : MarketUiState
    data class Error(val message: String) : MarketUiState
    data class Success(val instruments: List<InstrumentSummaryUiModel>) : MarketUiState
}
