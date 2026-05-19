package com.example.rmp_frontend.domain.model

data class Portfolio(
    val balance: Double,
    val currency: String,
    val items: List<PortfolioItem>,
) {
    val totalValue: Double = balance + items.sumOf { it.currentValue }
}
