package com.example.rmp_frontend.domain.model

data class Instrument(
    val id: String,
    val ticker: String,
    val name: String,
    val price: Double,
    val currency: String,
    val dailyChangePercent: Double,
)
