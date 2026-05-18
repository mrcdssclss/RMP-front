package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rmp_frontend.presentation.state.InstrumentDetailsUiModel
import com.example.rmp_frontend.presentation.state.InstrumentUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InstrumentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<InstrumentUiState>(InstrumentUiState.Loading)
    val uiState: StateFlow<InstrumentUiState> = _uiState.asStateFlow()

    fun loadInstrument(ticker: String) {
        val instrument = sampleDetails(ticker)
        _uiState.value = if (instrument == null) {
            InstrumentUiState.Empty
        } else {
            InstrumentUiState.Success(instrument = instrument)
        }
    }

    fun onPeriodClick(period: String) {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        _uiState.value = state.copy(selectedPeriod = period, operationMessage = null, operationError = null)
    }

    fun onQuantityChange(value: String) {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        val filteredValue = value.filter { it.isDigit() || it == '.' }.take(10)
        _uiState.value = state.copy(quantity = filteredValue, operationMessage = null, operationError = null)
    }

    fun onBuyClick() {
        submitOperation("Buy")
    }

    fun onSellClick() {
        submitOperation("Sell")
    }

    fun onRefreshClick() {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        loadInstrument(state.instrument.ticker)
    }

    private fun submitOperation(label: String) {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        if (state.quantity.isBlank() || state.quantity.toDoubleOrNull() == null || state.quantity.toDouble() <= 0.0) {
            _uiState.value = state.copy(operationError = "Enter quantity", operationMessage = null)
            return
        }

        _uiState.value = state.copy(
            operationMessage = "$label request sent to ViewModel",
            operationError = null
        )
    }

    private fun sampleDetails(ticker: String): InstrumentDetailsUiModel? {
        val normalizedTicker = ticker.uppercase()
        val base = when (normalizedTicker) {
            "AAPL" -> InstrumentDetailsUiModel("AAPL", "Apple Inc.", 189.45, 1.24, listOf(2f, 3f, 2.6f, 4.2f, 4.8f, 5.1f, 6.0f))
            "TSLA" -> InstrumentDetailsUiModel("TSLA", "Tesla", 351.10, -1.18, listOf(6f, 5.7f, 5.4f, 4.8f, 4.6f, 4.1f, 3.7f))
            "NVDA" -> InstrumentDetailsUiModel("NVDA", "NVIDIA", 132.80, 2.67, listOf(3f, 3.3f, 3.9f, 4.4f, 4.2f, 5.4f, 6.4f))
            "SBER" -> InstrumentDetailsUiModel("SBER", "Sberbank", 312.30, 0.54, listOf(4f, 4.2f, 4.1f, 4.5f, 4.7f, 4.9f, 5.0f))
            "GAZP" -> InstrumentDetailsUiModel("GAZP", "Gazprom", 146.20, -0.83, listOf(5f, 4.8f, 4.5f, 4.3f, 4.4f, 4.0f, 3.9f))
            else -> null
        }
        return base
    }
}
