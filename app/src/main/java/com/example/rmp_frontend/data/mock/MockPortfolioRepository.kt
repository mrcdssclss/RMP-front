package com.example.rmp_frontend.data.mock

import com.example.rmp_frontend.domain.model.Portfolio
import com.example.rmp_frontend.domain.model.PortfolioItem
import com.example.rmp_frontend.domain.repository.MarketRepository
import com.example.rmp_frontend.domain.repository.PortfolioRepository

class MockPortfolioRepository(
    private val marketRepository: MarketRepository,
) : PortfolioRepository {
    override suspend fun getPortfolio(): Portfolio {
        val instruments = marketRepository.getInstruments()
        return Portfolio(
            balance = 0.0,
            currency = instruments.firstOrNull()?.currency ?: "USD",
            items = instruments.map { instrument ->
                PortfolioItem(
                    instrument = instrument,
                    quantity = 2,
                    averagePrice = instrument.price,
                )
            },
        )
    }
}
