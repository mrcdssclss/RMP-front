package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.model.ChartPeriod
import com.example.rmp_frontend.domain.repository.InstrumentRepository
import com.example.rmp_frontend.domain.repository.TradingRepository
import com.example.rmp_frontend.presentation.state.InstrumentDetailsUiModel
import com.example.rmp_frontend.presentation.state.InstrumentUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InstrumentViewModel(
    private val instrumentRepository: InstrumentRepository,
    private val tradingRepository: TradingRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<InstrumentUiState>(InstrumentUiState.Loading)
    val uiState: StateFlow<InstrumentUiState> = _uiState.asStateFlow()
    private var currentTicker: String? = null

    fun loadInstrument(ticker: String) {
        currentTicker = ticker
        _uiState.value = InstrumentUiState.Loading
        viewModelScope.launch {
            runCatching {
                val instrument = instrumentRepository.getInstrument(ticker)
                val history = instrumentRepository.getPriceHistory(ticker, ChartPeriod.Day)
                InstrumentDetailsUiModel(
                    ticker = instrument.ticker,
                    name = instrument.name,
                    price = instrument.price,
                    changePercent = instrument.dailyChangePercent,
                    chartPoints = history.map { it.price.toFloat() },
                )
            }.onSuccess { details ->
                _uiState.value = InstrumentUiState.Success(details)
            }.onFailure { error ->
                _uiState.value = InstrumentUiState.Error(error.toUserMessage())
            }
        }
    }

    fun onPeriodSelected(period: String) {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        val ticker = currentTicker ?: state.instrument.ticker
        viewModelScope.launch {
            runCatching { instrumentRepository.getPriceHistory(ticker, period.toChartPeriod()) }
                .onSuccess { history ->
                    val latest = _uiState.value as? InstrumentUiState.Success ?: return@onSuccess
                    _uiState.value = latest.copy(
                        instrument = latest.instrument.copy(chartPoints = history.map { it.price.toFloat() }),
                        selectedPeriod = period,
                        operationError = null,
                    )
                }
                .onFailure { error ->
                    _uiState.value = state.copy(selectedPeriod = period, operationError = error.toUserMessage())
                }
        }
    }

    fun onQuantityChange(value: String) {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        _uiState.value = state.copy(
            quantity = value.filter { it.isDigit() || it == '.' }.take(10),
            operationMessage = null,
            operationError = null,
        )
    }

    fun onBuyClick(quantity: Double) = trade(quantity, isBuy = true)

    fun onSellClick(quantity: Double) = trade(quantity, isBuy = false)

    fun onRefreshClick() {
        val ticker = currentTicker ?: return
        loadInstrument(ticker)
    }

    private fun trade(quantity: Double, isBuy: Boolean) {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        if (quantity <= 0.0) {
            _uiState.value = state.copy(operationError = "Enter quantity", operationMessage = null)
            return
        }
        _uiState.value = state.copy(
            isOperationLoading = true,
            operationMessage = "Submitting order...",
            operationError = null,
        )
        viewModelScope.launch {
            runCatching {
                if (isBuy) {
                    tradingRepository.buyInstrument(state.instrument.ticker, quantity)
                } else {
                    tradingRepository.sellInstrument(state.instrument.ticker, quantity)
                }
            }.onSuccess { result ->
                val latest = _uiState.value as? InstrumentUiState.Success ?: return@onSuccess
                _uiState.value = latest.copy(
                    isOperationLoading = false,
                    operationMessage = "${result.type.name}: ${result.quantity} ${state.instrument.ticker} executed",
                    operationError = null,
                )
            }.onFailure { error ->
                val latest = _uiState.value as? InstrumentUiState.Success ?: state
                _uiState.value = latest.copy(
                    isOperationLoading = false,
                    operationMessage = null,
                    operationError = error.toUserMessage(),
                )
            }
        }
    }
}

private fun String.toChartPeriod(): ChartPeriod = when (this) {
    "1W" -> ChartPeriod.Week
    "1M" -> ChartPeriod.Month
    "1Y" -> ChartPeriod.Year
    "ALL" -> ChartPeriod.All
    else -> ChartPeriod.Day
}
