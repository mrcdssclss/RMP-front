package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.repository.PortfolioRepository
import com.example.rmp_frontend.presentation.state.PortfolioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PortfolioUiState())
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    fun loadPortfolio() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { portfolioRepository.getPortfolio() }
                .onSuccess { portfolio ->
                    _uiState.value = PortfolioUiState(
                        balance = portfolio.balance,
                        currency = portfolio.currency,
                        assets = portfolio.items,
                        totalValue = portfolio.totalValue,
                        isEmpty = portfolio.items.isEmpty(),
                    )
                }
                .onFailure { error -> _uiState.value = PortfolioUiState(errorMessage = error.toUserMessage()) }
        }
    }

    fun refresh() = loadPortfolio()
}
