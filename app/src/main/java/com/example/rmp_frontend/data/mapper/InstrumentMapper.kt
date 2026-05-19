package com.example.rmp_frontend.data.mapper

import com.example.rmp_frontend.data.dto.InstrumentDto
import com.example.rmp_frontend.data.dto.PricePointDto
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.model.PricePoint

fun InstrumentDto.toDomain(): Instrument {
    return Instrument(
        id = id,
        ticker = ticker,
        name = name,
        price = price,
        currency = currency ?: "RUB",
        dailyChangePercent = dailyChangePercent ?: 0.0,
    )
}

fun PricePointDto.toDomain(): PricePoint {
    return PricePoint(
        timestampMillis = timestampMillis,
        price = price,
    )
}
