package com.example.rmp_frontend.data.dto

data class InstrumentDto(
    val id: String,
    val ticker: String,
    val name: String,
    val price: Double,
    val currency: String? = null,
    val dailyChangePercent: Double? = null,
)

data class PricePointDto(
    val timestampMillis: Long,
    val price: Double,
)
