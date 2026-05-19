package com.example.rmp_frontend.data.mock

import com.example.rmp_frontend.domain.model.TradeResult
import com.example.rmp_frontend.domain.model.TransactionType
import com.example.rmp_frontend.domain.repository.TradingRepository
import kotlinx.coroutines.delay

class MockTradingRepository : TradingRepository {
    override suspend fun buyInstrument(instrumentId: String, quantity: Int): TradeResult {
        return trade(instrumentId, quantity, TransactionType.Buy)
    }

    override suspend fun sellInstrument(instrumentId: String, quantity: Int): TradeResult {
        return trade(instrumentId, quantity, TransactionType.Sell)
    }

    private suspend fun trade(instrumentId: String, quantity: Int, type: TransactionType): TradeResult {
        delay(250)
        require(quantity > 0) { "Количество должно быть больше 0" }
        val instrument = MockData.instruments.firstOrNull { it.id == instrumentId }
            ?: error("Инструмент не найден")
        return TradeResult(
            orderId = "mock-order-${System.currentTimeMillis()}",
            instrumentId = instrumentId,
            quantity = quantity,
            executedPrice = instrument.price,
            type = type,
            message = "Заявка выполнена",
        )
    }
}
