package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.InstrumentApi
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.domain.model.ChartPeriod
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.model.PricePoint
import com.example.rmp_frontend.domain.repository.InstrumentRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class InstrumentRepositoryImpl(
    private val instrumentApi: InstrumentApi,
) : InstrumentRepository {
    override suspend fun getInstrument(instrumentId: String): Instrument {
        return instrumentApi.getInstruments()
            .firstOrNull { it.ticker.equals(instrumentId, ignoreCase = true) }
            ?.toDomain()
            ?: error("Instrument not found")
    }

    override suspend fun getPriceHistory(instrumentId: String, period: ChartPeriod): List<PricePoint> {
        val to = Date()
        val from = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            time = to
            add(
                when (period) {
                    ChartPeriod.Day -> Calendar.DAY_OF_YEAR
                    ChartPeriod.Week -> Calendar.WEEK_OF_YEAR
                    ChartPeriod.Month -> Calendar.MONTH
                    ChartPeriod.Year -> Calendar.YEAR
                    ChartPeriod.All -> Calendar.YEAR
                },
                when (period) {
                    ChartPeriod.All -> -10
                    else -> -1
                },
            )
        }.time
        return instrumentApi.getPriceHistory(instrumentId, from.toApiTimestamp(), to.toApiTimestamp())
            .map { it.toDomain() }
    }
}

private fun Date.toApiTimestamp(): String =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(this)
