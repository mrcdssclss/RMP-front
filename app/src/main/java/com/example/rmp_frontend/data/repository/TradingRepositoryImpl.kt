package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.TradingApi
import com.example.rmp_frontend.data.dto.TradeRequestDto
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.domain.model.TradeResult
import com.example.rmp_frontend.domain.repository.TradingRepository

class TradingRepositoryImpl(
    private val tradingApi: TradingApi,
) : TradingRepository {
    override suspend fun buyInstrument(instrumentId: String, quantity: Int): TradeResult {
        return tradingApi.buy(TradeRequestDto(instrumentId, quantity)).toDomain()
    }

    override suspend fun sellInstrument(instrumentId: String, quantity: Int): TradeResult {
        return tradingApi.sell(TradeRequestDto(instrumentId, quantity)).toDomain()
    }
}
