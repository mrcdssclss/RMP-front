package com.example.rmp_frontend.data.dto

data class TradeRequestDto(
    val instrumentId: String,
    val quantity: Int,
)

data class TradeResultDto(
    val orderId: String,
    val instrumentId: String,
    val quantity: Int,
    val executedPrice: Double,
    val type: String,
    val message: String? = null,
)
