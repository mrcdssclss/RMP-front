package com.example.rmp_frontend.data.dto

data class PortfolioDto(
    val balance: Double,
    val currency: String? = null,
    val items: List<PortfolioItemDto> = emptyList(),
)

data class PortfolioItemDto(
    val instrument: InstrumentDto,
    val quantity: Int,
    val averagePrice: Double,
)
