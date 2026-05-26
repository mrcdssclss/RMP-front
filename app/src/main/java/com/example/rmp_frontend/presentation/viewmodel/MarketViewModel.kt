package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rmp_frontend.presentation.state.InstrumentSummaryUiModel
import com.example.rmp_frontend.presentation.state.MarketUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MarketViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MarketUiState>(MarketUiState.Loading)
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()

    init {
        loadMarket()
    }

    fun onRefreshClick() {
        loadMarket()
    }

    private fun loadMarket() {
        val instruments = listOf(
            InstrumentSummaryUiModel("AAPL", "Apple Inc.", 189.45, 1.24),
            InstrumentSummaryUiModel("TSLA", "Tesla", 351.10, -1.18),
            InstrumentSummaryUiModel("NVDA", "NVIDIA", 132.80, 2.67),
            InstrumentSummaryUiModel("SBER", "Sberbank", 312.30, 0.54),
            InstrumentSummaryUiModel("GAZP", "Gazprom", 146.20, -0.83)
        )

        _uiState.value = if (instruments.isEmpty()) {
            MarketUiState.Empty
        } else {
            MarketUiState.Success(instruments)
        }
    }
}
