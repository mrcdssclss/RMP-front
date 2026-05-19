package com.example.rmp_frontend.domain.model

data class PricePoint(
    val timestampMillis: Long,
    val price: Double,
)

enum class ChartPeriod(val apiValue: String) {
    Day("1d"),
    Week("1w"),
    Month("1m"),
    Year("1y"),
}
