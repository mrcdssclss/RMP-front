package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.model.ChartPeriod
import com.example.rmp_frontend.domain.repository.InstrumentRepository
import com.example.rmp_frontend.domain.repository.TradingRepository
import com.example.rmp_frontend.presentation.state.InstrumentUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InstrumentViewModel(
    private val instrumentRepository: InstrumentRepository,
    private val tradingRepository: TradingRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(InstrumentUiState())
    val uiState: StateFlow<InstrumentUiState> = _uiState.asStateFlow()

    fun loadInstrument(instrumentId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { instrumentRepository.getInstrument(instrumentId) }
                .onSuccess { instrument ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        instrument = instrument,
                        isEmpty = false,
                        errorMessage = null,
                    )
                    loadPriceHistory(instrumentId, _uiState.value.selectedPeriod)
                }
                .onFailure { error -> _uiState.value = InstrumentUiState(errorMessage = error.toUserMessage()) }
        }
    }

    fun loadPriceHistory(instrumentId: String, period: ChartPeriod) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, selectedPeriod = period, errorMessage = null)
            runCatching { instrumentRepository.getPriceHistory(instrumentId, period) }
                .onSuccess { history ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        priceHistory = history,
                        isEmpty = _uiState.value.instrument == null && history.isEmpty(),
                        errorMessage = null,
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = error.toUserMessage())
                }
        }
    }

    fun changePeriod(period: ChartPeriod) {
        val instrumentId = _uiState.value.instrument?.id
        _uiState.value = _uiState.value.copy(selectedPeriod = period)
        if (instrumentId != null) {
            loadPriceHistory(instrumentId, period)
        }
    }

    fun buyInstrument(instrumentId: String, quantity: Int) {
        trade(instrumentId, quantity, isBuy = true)
    }

    fun sellInstrument(instrumentId: String, quantity: Int) {
        trade(instrumentId, quantity, isBuy = false)
    }

    private fun trade(instrumentId: String, quantity: Int, isBuy: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, lastTradeResult = null)
            runCatching {
                if (isBuy) {
                    tradingRepository.buyInstrument(instrumentId, quantity)
                } else {
                    tradingRepository.sellInstrument(instrumentId, quantity)
                }
            }
                .onSuccess { result ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        lastTradeResult = result,
                        errorMessage = null,
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = error.toUserMessage())
                }
        }
    }
}
