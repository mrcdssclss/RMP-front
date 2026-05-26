package com.example.rmp_frontend.domain.repository

import com.example.rmp_frontend.domain.model.Instrument
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    suspend fun getInstruments(): List<Instrument>
    fun priceUpdates(): Flow<Instrument>
    fun subscribeToPriceUpdates()
    fun unsubscribeFromPriceUpdates()
}
