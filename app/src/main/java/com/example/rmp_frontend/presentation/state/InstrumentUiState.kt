package com.example.rmp_frontend.presentation.state

sealed interface InstrumentUiState {
    data object Loading : InstrumentUiState
    data object Empty : InstrumentUiState
    data class Error(val message: String) : InstrumentUiState

    data class Success(
        val instrument: InstrumentDetailsUiModel,
        val selectedPeriod: String = "1D",
        val quantity: String = "",
        val operationMessage: String? = null,
        val operationError: String? = null
    ) : InstrumentUiState
}
