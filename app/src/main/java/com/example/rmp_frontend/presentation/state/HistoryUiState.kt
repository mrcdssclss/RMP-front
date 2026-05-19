package com.example.rmp_frontend.presentation.state

import com.example.rmp_frontend.domain.model.Transaction

data class HistoryUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
)
