package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.repository.PortfolioRepository
import com.example.rmp_frontend.presentation.state.PortfolioAssetUiModel
import com.example.rmp_frontend.presentation.state.PortfolioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<PortfolioUiState>(PortfolioUiState.Loading)
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    init {
        loadPortfolio()
    }

    fun onRefreshClick() = loadPortfolio()

    private fun loadPortfolio() {
        _uiState.value = PortfolioUiState.Loading
        viewModelScope.launch {
            runCatching { portfolioRepository.getPortfolio() }
                .onSuccess { portfolio ->
                    val assets = portfolio.items.map {
                        val invested = it.averagePrice * it.quantity
                        val profitLoss = it.currentValue - invested
                        PortfolioAssetUiModel(
                            ticker = it.instrument.ticker,
                            name = it.instrument.name,
                            quantity = it.quantity.toDouble(),
                            positionValue = it.currentValue,
                            profitLoss = profitLoss,
                            profitLossPercent = if (invested == 0.0) 0.0 else profitLoss / invested * 100,
                        )
                    }
                    _uiState.value = if (assets.isEmpty()) {
                        PortfolioUiState.Empty
                    } else {
                        PortfolioUiState.Success(portfolio.balance, portfolio.totalValue, assets)
                    }
                }
                .onFailure { _uiState.value = PortfolioUiState.Error(it.toUserMessage()) }
        }
    }
}
