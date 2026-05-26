package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.model.TransactionType as DomainTransactionType
import com.example.rmp_frontend.domain.repository.HistoryRepository
import com.example.rmp_frontend.presentation.state.HistoryUiState
import com.example.rmp_frontend.presentation.state.TransactionType
import com.example.rmp_frontend.presentation.state.TransactionUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun onRefreshClick() = loadHistory()

    private fun loadHistory() {
        _uiState.value = HistoryUiState.Loading
        viewModelScope.launch {
            runCatching { historyRepository.getTransactions() }
                .onSuccess { transactions ->
                    val uiModels = transactions.map {
                        TransactionUiModel(
                            id = it.id,
                            date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(it.timestampMillis)),
                            type = if (it.type == DomainTransactionType.Buy) TransactionType.BUY else TransactionType.SELL,
                            ticker = it.ticker,
                            quantity = it.quantity,
                            price = it.price,
                            total = it.price * it.quantity,
                        )
                    }
                    _uiState.value = if (uiModels.isEmpty()) HistoryUiState.Empty else HistoryUiState.Success(uiModels)
                }
                .onFailure { _uiState.value = HistoryUiState.Error(it.toUserMessage()) }
        }
    }
}
