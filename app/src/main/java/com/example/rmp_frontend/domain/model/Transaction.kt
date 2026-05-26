package com.example.rmp_frontend.domain.model

data class Transaction(
    val id: String,
    val instrumentId: String,
    val ticker: String,
    val type: TransactionType,
    val quantity: Double,
    val price: Double,
    val timestampMillis: Long,
)

enum class TransactionType {
    Buy,
    Sell,
}
