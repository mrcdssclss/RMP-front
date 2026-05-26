package com.example.rmp_frontend.domain.repository

import com.example.rmp_frontend.domain.model.TradeResult

interface TradingRepository {
    suspend fun buyInstrument(instrumentId: String, quantity: Double): TradeResult
    suspend fun sellInstrument(instrumentId: String, quantity: Double): TradeResult
}
