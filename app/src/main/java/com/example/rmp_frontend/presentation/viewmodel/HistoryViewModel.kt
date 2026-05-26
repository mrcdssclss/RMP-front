package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rmp_frontend.presentation.state.HistoryUiState
import com.example.rmp_frontend.presentation.state.TransactionType
import com.example.rmp_frontend.presentation.state.TransactionUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HistoryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun onRefreshClick() {
        loadHistory()
    }

    private fun loadHistory() {
        val transactions = listOf(
            TransactionUiModel("1", "2026-05-18", TransactionType.BUY, "AAPL", 2.0, 186.20, 372.40),
            TransactionUiModel("2", "2026-05-16", TransactionType.SELL, "TSLA", 1.0, 354.00, 354.00),
            TransactionUiModel("3", "2026-05-14", TransactionType.BUY, "NVDA", 4.0, 128.70, 514.80)
        )

        _uiState.value = if (transactions.isEmpty()) {
            HistoryUiState.Empty
        } else {
            HistoryUiState.Success(transactions)
        }
    }
}
