package com.example.rmp_frontend.data.dto

data class TransactionDto(
    val id: String,
    val instrumentId: String,
    val ticker: String,
    val type: String,
    val quantity: Double,
    val price: Double,
    val timestampMillis: Long,
)
