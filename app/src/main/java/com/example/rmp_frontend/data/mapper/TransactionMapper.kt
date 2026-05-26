package com.example.rmp_frontend.data.mapper

import com.example.rmp_frontend.data.dto.TradeResultDto
import com.example.rmp_frontend.data.dto.TransactionDto
import com.example.rmp_frontend.domain.model.TradeResult
import com.example.rmp_frontend.domain.model.Transaction
import com.example.rmp_frontend.domain.model.TransactionType

fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = id,
        instrumentId = instrumentId,
        ticker = ticker,
        type = type.toTransactionType(),
        quantity = quantity,
        price = price,
        timestampMillis = timestampMillis,
    )
}

fun TradeResultDto.toDomain(): TradeResult {
    return TradeResult(
        orderId = orderId,
        instrumentId = instrumentId,
        quantity = quantity,
        executedPrice = executedPrice,
        type = type.toTransactionType(),
        message = message ?: "Order executed",
    )
}

fun String.toTransactionType(): TransactionType {
    return when (lowercase()) {
        "sell" -> TransactionType.Sell
        else -> TransactionType.Buy
    }
}
