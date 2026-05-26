package com.example.rmp_frontend.presentation.state

sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data object Empty : HistoryUiState
    data class Error(val message: String) : HistoryUiState
    data class Success(val transactions: List<TransactionUiModel>) : HistoryUiState
}
