package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.repository.MarketRepository
import com.example.rmp_frontend.presentation.state.InstrumentSummaryUiModel
import com.example.rmp_frontend.presentation.state.MarketUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MarketViewModel(
    private val marketRepository: MarketRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MarketUiState>(MarketUiState.Loading)
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()
    private var updatesJob: Job? = null

    init {
        loadMarket()
        marketRepository.subscribeToPriceUpdates()
        updatesJob = viewModelScope.launch {
            marketRepository.priceUpdates().collect { instrument ->
                val state = _uiState.value as? MarketUiState.Success ?: return@collect
                _uiState.value = state.copy(
                    instruments = state.instruments.map {
                        if (it.ticker == instrument.ticker) it.copy(price = instrument.price) else it
                    },
                )
            }
        }
    }

    fun onRefreshClick() = loadMarket()

    private fun loadMarket() {
        _uiState.value = MarketUiState.Loading
        viewModelScope.launch {
            runCatching { marketRepository.getInstruments() }
                .onSuccess { instruments ->
                    val uiModels = instruments.map {
                        InstrumentSummaryUiModel(it.ticker, it.name, it.price, it.dailyChangePercent)
                    }
                    _uiState.value = if (uiModels.isEmpty()) MarketUiState.Empty else MarketUiState.Success(uiModels)
                }
                .onFailure { _uiState.value = MarketUiState.Error(it.toUserMessage()) }
        }
    }

    override fun onCleared() {
        marketRepository.unsubscribeFromPriceUpdates()
        updatesJob?.cancel()
        super.onCleared()
    }
}
