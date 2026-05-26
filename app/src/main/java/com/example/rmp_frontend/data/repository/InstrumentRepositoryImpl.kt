package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.InstrumentApi
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.domain.model.ChartPeriod
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.model.PricePoint
import com.example.rmp_frontend.domain.repository.InstrumentRepository

class InstrumentRepositoryImpl(
    private val instrumentApi: InstrumentApi,
) : InstrumentRepository {
    override suspend fun getInstrument(instrumentId: String): Instrument {
        return instrumentApi.getInstrument(instrumentId).toDomain()
    }

    override suspend fun getPriceHistory(instrumentId: String, period: ChartPeriod): List<PricePoint> {
        return instrumentApi.getPriceHistory(instrumentId, period.apiValue).map { it.toDomain() }
    }
}
