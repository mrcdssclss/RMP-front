package com.example.rmp_frontend.data.mock

import com.example.rmp_frontend.domain.model.Instrument
import com.example.rmp_frontend.domain.model.Portfolio
import com.example.rmp_frontend.domain.model.PortfolioItem
import com.example.rmp_frontend.domain.model.Transaction
import com.example.rmp_frontend.domain.model.TransactionType
import com.example.rmp_frontend.domain.model.User

object MockData {
    val instruments = listOf(
        Instrument("sber", "SBER", "Сбербанк", 302.40, "RUB", 1.25),
        Instrument("gazp", "GAZP", "Газпром", 166.12, "RUB", -0.48),
        Instrument("lkoh", "LKOH", "Лукойл", 7120.00, "RUB", 0.82),
        Instrument("yndx", "YNDX", "Яндекс", 4388.50, "RUB", 2.14),
    )

    val user = User(
        id = "demo-user",
        login = "demo",
        displayName = "Demo Investor",
        email = "demo@example.com",
    )

    val portfolio = Portfolio(
        balance = 125_000.0,
        currency = "RUB",
        items = listOf(
            PortfolioItem(instruments[0], quantity = 12, averagePrice = 285.0),
            PortfolioItem(instruments[2], quantity = 2, averagePrice = 6900.0),
            PortfolioItem(instruments[3], quantity = 1, averagePrice = 4100.0),
        ),
    )

    val transactions = listOf(
        Transaction("tr-1", "sber", "SBER", TransactionType.Buy, 10, 281.20, 1_720_000_000_000),
        Transaction("tr-2", "lkoh", "LKOH", TransactionType.Buy, 2, 6900.00, 1_720_086_400_000),
        Transaction("tr-3", "sber", "SBER", TransactionType.Buy, 2, 303.10, 1_720_172_800_000),
    )
}
