package com.example.rmp_frontend.domain.model

data class PortfolioItem(
    val instrument: Instrument,
    val quantity: Int,
    val averagePrice: Double,
) {
    val currentValue: Double = instrument.price * quantity
}
