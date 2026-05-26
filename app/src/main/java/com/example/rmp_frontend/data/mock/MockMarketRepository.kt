package com.example.rmp_frontend.data.mock

import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.repository.MarketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MockMarketRepository : MarketRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val updates = MutableSharedFlow<Instrument>(extraBufferCapacity = 32)
    private var updateJob: Job? = null

    override suspend fun getInstruments(): List<Instrument> {
        delay(250)
        return MockData.instruments
    }

    override fun priceUpdates(): Flow<Instrument> = updates

    override fun subscribeToPriceUpdates() {
        if (updateJob != null) return
        updateJob = scope.launch {
            var tick = 0
            while (isActive) {
                delay(1500)
                val base = MockData.instruments[tick % MockData.instruments.size]
                val delta = ((tick % 5) - 2) * 0.35
                updates.emit(
                    base.copy(
                        price = (base.price + delta).coerceAtLeast(1.0),
                        dailyChangePercent = base.dailyChangePercent + delta / 10,
                    ),
                )
                tick++
            }
        }
    }

    override fun unsubscribeFromPriceUpdates() {
        updateJob?.cancel()
        updateJob = null
    }
}
