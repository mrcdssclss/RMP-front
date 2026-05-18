package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rmp_frontend.presentation.state.PortfolioAssetUiModel
import com.example.rmp_frontend.presentation.state.PortfolioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PortfolioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<PortfolioUiState>(PortfolioUiState.Loading)
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    init {
        loadPortfolio()
    }

    fun onRefreshClick() {
        loadPortfolio()
    }

    private fun loadPortfolio() {
        val assets = listOf(
            PortfolioAssetUiModel("AAPL", "Apple Inc.", 8.0, 1515.60, 86.20, 6.03),
            PortfolioAssetUiModel("NVDA", "NVIDIA", 14.0, 1859.20, 244.50, 15.14),
            PortfolioAssetUiModel("TSLA", "Tesla", 3.0, 1053.30, -37.80, -3.46)
        )

        _uiState.value = if (assets.isEmpty()) {
            PortfolioUiState.Empty
        } else {
            PortfolioUiState.Success(
                cashBalance = 4250.00,
                totalValue = 8678.10,
                assets = assets
            )
        }
    }
}
