package com.example.rmp_frontend.data.mock

import com.example.rmp_frontend.domain.model.ChartPeriod
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.model.PricePoint
import com.example.rmp_frontend.domain.repository.InstrumentRepository
import kotlinx.coroutines.delay

class MockInstrumentRepository : InstrumentRepository {
    override suspend fun getInstrument(instrumentId: String): Instrument {
        delay(200)
        return MockData.instruments.firstOrNull { it.id == instrumentId }
            ?: error("Инструмент не найден")
    }

    override suspend fun getPriceHistory(instrumentId: String, period: ChartPeriod): List<PricePoint> {
        delay(200)
        val instrument = getInstrument(instrumentId)
        val step = when (period) {
            ChartPeriod.Day -> 60 * 60 * 1000L
            ChartPeriod.Week -> 24 * 60 * 60 * 1000L
            ChartPeriod.Month -> 24 * 60 * 60 * 1000L
            ChartPeriod.Year -> 30 * 24 * 60 * 60 * 1000L
        }
        val points = when (period) {
            ChartPeriod.Day -> 24
            ChartPeriod.Week -> 7
            ChartPeriod.Month -> 30
            ChartPeriod.Year -> 12
        }
        val now = System.currentTimeMillis()
        return List(points) { index ->
            val offset = index - points / 2
            PricePoint(
                timestampMillis = now - (points - index) * step,
                price = (instrument.price + offset * 1.7).coerceAtLeast(1.0),
            )
        }
    }
}
