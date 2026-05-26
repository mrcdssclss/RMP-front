package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.MarketApi
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.data.websocket.PriceWebSocketClient
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class MarketRepositoryImpl(
    private val marketApi: MarketApi,
    private val priceWebSocketClient: PriceWebSocketClient,
) : MarketRepository {
    private val instrumentsByTicker = mutableMapOf<String, Instrument>()

    override suspend fun getInstruments(): List<Instrument> {
        return marketApi.getInstruments().map { it.toDomain() }.also { instruments ->
            instruments.forEach { instrumentsByTicker[it.ticker] = it }
        }
    }

    override fun priceUpdates(): Flow<Instrument> = priceWebSocketClient.priceUpdates.mapNotNull { update ->
        instrumentsByTicker[update.ticker]?.copy(price = update.price)?.also {
            instrumentsByTicker[update.ticker] = it
        }
    }

    override fun subscribeToPriceUpdates() {
        priceWebSocketClient.connect()
    }

    override fun unsubscribeFromPriceUpdates() {
        priceWebSocketClient.disconnect()
    }
}
