package com.example.rmp_frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.repository.MarketRepository
import com.example.rmp_frontend.presentation.state.MarketUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarketViewModel(
    private val marketRepository: MarketRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MarketUiState())
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()
    private var updatesJob: Job? = null

    fun loadInstruments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { marketRepository.getInstruments() }
                .onSuccess { instruments ->
                    _uiState.value = MarketUiState(
                        instruments = instruments,
                        isEmpty = instruments.isEmpty(),
                    )
                }
                .onFailure { error -> _uiState.value = MarketUiState(errorMessage = error.toUserMessage()) }
        }
    }

    fun refresh() = loadInstruments()

    fun subscribeToPriceUpdates() {
        marketRepository.subscribeToPriceUpdates()
        if (updatesJob != null) return
        updatesJob = viewModelScope.launch {
            marketRepository.priceUpdates().collect { updated ->
                _uiState.value = _uiState.value.copy(
                    instruments = _uiState.value.instruments.upsert(updated),
                    errorMessage = null,
                    isEmpty = false,
                )
            }
        }
    }

    fun unsubscribeFromPriceUpdates() {
        marketRepository.unsubscribeFromPriceUpdates()
        updatesJob?.cancel()
        updatesJob = null
    }

    override fun onCleared() {
        unsubscribeFromPriceUpdates()
        super.onCleared()
    }
}

private fun List<Instrument>.upsert(updated: Instrument): List<Instrument> {
    val index = indexOfFirst { it.id == updated.id }
    return if (index == -1) this + updated else toMutableList().also { it[index] = updated }
}
