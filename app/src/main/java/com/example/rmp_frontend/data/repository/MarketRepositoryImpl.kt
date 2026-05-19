package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.MarketApi
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.data.websocket.PriceWebSocketClient
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow

class MarketRepositoryImpl(
    private val marketApi: MarketApi,
    private val priceWebSocketClient: PriceWebSocketClient,
) : MarketRepository {
    override suspend fun getInstruments(): List<Instrument> {
        return marketApi.getInstruments().map { it.toDomain() }
    }

    override fun priceUpdates(): Flow<Instrument> = priceWebSocketClient.priceUpdates

    override fun subscribeToPriceUpdates() {
        priceWebSocketClient.connect()
    }

    override fun unsubscribeFromPriceUpdates() {
        priceWebSocketClient.disconnect()
    }
}
