package com.example.rmp_frontend.data.mapper

import com.example.rmp_frontend.data.dto.InstrumentDto
import com.example.rmp_frontend.data.dto.PricePointDto
import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.model.PricePoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun InstrumentDto.toDomain(): Instrument {
    return Instrument(
        id = ticker,
        ticker = ticker,
        name = name,
        price = latestPrice ?: 0.0,
        currency = currency,
        dailyChangePercent = 0.0,
    )
}

fun PricePointDto.toDomain(): PricePoint {
    return PricePoint(
        timestampMillis = at.toTimestampMillis(),
        price = price,
    )
}

internal fun String.toTimestampMillis(): Long {
    val zone = if (endsWith("Z")) "Z" else takeLast(6).takeIf { it.firstOrNull() in listOf('+', '-') } ?: "Z"
    val withoutZone = removeSuffix(zone)
    val base = withoutZone.substringBefore('.')
    val fraction = withoutZone.substringAfter('.', "")
        .padEnd(3, '0')
        .take(3)
    val normalized = "$base.$fraction$zone"
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse(normalized)?.time ?: 0L
}
