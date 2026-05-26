package com.example.rmp_frontend.data.mapper

import com.example.rmp_frontend.data.dto.PortfolioDto
import com.example.rmp_frontend.data.dto.PortfolioItemDto
import com.example.rmp_frontend.domain.model.Portfolio
import com.example.rmp_frontend.domain.model.PortfolioItem

fun PortfolioDto.toDomain(): Portfolio {
    return Portfolio(
        balance = balance,
        currency = currency ?: "RUB",
        items = items.map { it.toDomain() },
    )
}

fun PortfolioItemDto.toDomain(): PortfolioItem {
    return PortfolioItem(
        instrument = instrument.toDomain(),
        quantity = quantity,
        averagePrice = averagePrice,
    )
}
