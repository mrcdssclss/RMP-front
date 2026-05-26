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
    private var currentTicker: String? = null

    fun loadInstrument(ticker: String) {
        currentTicker = ticker
        val instrument = sampleDetails(ticker)
        _uiState.value = if (instrument == null) {
            InstrumentUiState.Empty
        } else {
            InstrumentUiState.Success(instrument = instrument)
        }
    }

    fun onPeriodSelected(period: String) {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        _uiState.value = state.copy(
            instrument = state.instrument.copy(chartPoints = chartPointsFor(period)),
            selectedPeriod = period,
            operationMessage = null,
            operationError = null
        )
    }

    fun onQuantityChange(value: String) {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        val filteredValue = value.filter { it.isDigit() || it == '.' }.take(10)
        _uiState.value = state.copy(quantity = filteredValue, operationMessage = null, operationError = null)
    }

    fun onBuyClick(quantity: Double) {
        submitOperation(
            quantity = quantity,
            successMessage = "Заявка на покупку отправлена"
        )
    }

    fun onSellClick(quantity: Double) {
        submitOperation(
            quantity = quantity,
            successMessage = "Заявка на продажу отправлена"
        )
    }

    fun onRefreshClick() {
        val ticker = currentTicker ?: (_uiState.value as? InstrumentUiState.Success)?.instrument?.ticker ?: return
        loadInstrument(ticker)
    }

    private fun submitOperation(
        quantity: Double,
        successMessage: String
    ) {
        val state = _uiState.value as? InstrumentUiState.Success ?: return
        if (quantity <= 0.0) {
            _uiState.value = state.copy(operationError = "Enter quantity", operationMessage = null)
            return
        }

        // UI stub only: Developer 2 should replace this with a real order flow outside Composables.
        _uiState.value = state.copy(
            operationMessage = successMessage,
            operationError = null
        )
    }

    private fun sampleDetails(ticker: String): InstrumentDetailsUiModel? {
        val normalizedTicker = ticker.uppercase()
        val base = when (normalizedTicker) {
            "AAPL" -> InstrumentDetailsUiModel("AAPL", "Apple Inc.", 189.45, 1.24, chartPointsFor("1D"))
            "TSLA" -> InstrumentDetailsUiModel("TSLA", "Tesla", 351.10, -1.18, chartPointsFor("1D"))
            "NVDA" -> InstrumentDetailsUiModel("NVDA", "NVIDIA", 132.80, 2.67, chartPointsFor("1D"))
            "SBER" -> InstrumentDetailsUiModel("SBER", "Sberbank", 312.30, 0.54, chartPointsFor("1D"))
            "GAZP" -> InstrumentDetailsUiModel("GAZP", "Gazprom", 146.20, -0.83, chartPointsFor("1D"))
            else -> null
        }
        return base
    }

    private fun chartPointsFor(period: String): List<Float> {
        return when (period) {
            "1D" -> listOf(4.0f, 4.8f, 4.1f, 5.0f, 4.5f, 5.6f, 5.1f, 6.0f)
            "1W" -> listOf(3.8f, 4.0f, 4.5f, 4.2f, 4.9f, 5.3f, 5.0f, 5.7f, 6.1f)
            "1M" -> listOf(2.8f, 3.1f, 3.0f, 3.6f, 3.9f, 4.3f, 4.0f, 4.7f, 5.2f, 5.4f, 5.1f, 5.8f, 6.2f)
            "1Y" -> listOf(2.5f, 2.8f, 3.1f, 3.5f, 3.9f, 4.1f, 4.4f, 4.8f, 5.0f, 5.4f, 5.8f, 6.0f)
            "ALL" -> listOf(1.4f, 1.8f, 2.2f, 2.0f, 2.7f, 3.2f, 3.8f, 3.5f, 4.2f, 4.8f, 5.5f, 6.1f)
            else -> chartPointsFor("1D")
        }
    }
}
