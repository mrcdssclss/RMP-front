package com.example.rmp_frontend.presentation.state

import com.example.rmp_frontend.domain.model.ChartPeriod
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.model.PricePoint
import com.example.rmp_frontend.domain.model.TradeResult

data class InstrumentUiState(
    val isLoading: Boolean = false,
    val instrument: Instrument? = null,
    val priceHistory: List<PricePoint> = emptyList(),
    val selectedPeriod: ChartPeriod = ChartPeriod.Day,
    val lastTradeResult: TradeResult? = null,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
)
