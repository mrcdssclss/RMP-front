package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.repository.HistoryRepository
import com.example.rmp_frontend.presentation.state.HistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { historyRepository.getTransactions() }
                .onSuccess { transactions ->
                    _uiState.value = HistoryUiState(
                        transactions = transactions,
                        isEmpty = transactions.isEmpty(),
                    )
                }
                .onFailure { error -> _uiState.value = HistoryUiState(errorMessage = error.toUserMessage()) }
        }
    }

    fun refresh() = loadTransactions()
}
