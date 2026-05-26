package com.example.rmp_frontend.data.dto

data class TradeRequestDto(
    val ticker: String,
    val quantity: Double,
)

data class TradeResultDto(
    val transactionId: String,
    val side: String,
    val ticker: String,
    val price: Double,
    val quantity: Double,
    val total: Double,
    val createdAt: String,
)
