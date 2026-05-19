package com.example.rmp_frontend.presentation.state

import com.example.rmp_frontend.domain.model.Instrument

data class MarketUiState(
    val isLoading: Boolean = false,
    val instruments: List<Instrument> = emptyList(),
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
)
