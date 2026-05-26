package com.example.rmp_frontend.domain.model

data class TradeResult(
    val orderId: String,
    val instrumentId: String,
    val quantity: Double,
    val executedPrice: Double,
    val type: TransactionType,
    val message: String,
)
