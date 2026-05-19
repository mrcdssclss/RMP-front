package com.example.rmp_frontend.domain.repository

import com.example.rmp_frontend.domain.model.ChartPeriod
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.model.PricePoint

interface InstrumentRepository {
    suspend fun getInstrument(instrumentId: String): Instrument
    suspend fun getPriceHistory(instrumentId: String, period: ChartPeriod): List<PricePoint>
}
