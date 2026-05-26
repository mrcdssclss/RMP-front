package com.example.rmp_frontend.data.dto

data class InstrumentDto(
    val ticker: String,
    val name: String,
    val currency: String,
    val latestPrice: Double? = null,
)

data class PricePointDto(
    val ticker: String,
    val price: Double,
    val at: String,
)
