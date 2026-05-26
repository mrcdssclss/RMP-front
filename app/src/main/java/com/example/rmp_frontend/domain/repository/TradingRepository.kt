package com.example.rmp_frontend.domain.repository

import com.example.rmp_frontend.domain.model.TradeResult

interface TradingRepository {
    suspend fun buyInstrument(instrumentId: String, quantity: Int): TradeResult
    suspend fun sellInstrument(instrumentId: String, quantity: Int): TradeResult
}
