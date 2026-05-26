package com.example.rmp_frontend.presentation.state


data class HistoryUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
)
