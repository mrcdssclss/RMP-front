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
        orderId = transactionId,
        instrumentId = ticker,
        quantity = quantity,
        executedPrice = price,
        type = side.toTransactionType(),
        message = "Order executed",
    )
}

fun TradeResultDto.toTransactionDomain(): Transaction {
    return Transaction(
        id = transactionId,
        instrumentId = ticker,
        ticker = ticker,
        type = side.toTransactionType(),
        quantity = quantity,
        price = price,
        timestampMillis = createdAt.toTimestampMillis(),
    )
}

fun String.toTransactionType(): TransactionType {
    return when (lowercase()) {
        "sell" -> TransactionType.Sell
        else -> TransactionType.Buy
    }
}
