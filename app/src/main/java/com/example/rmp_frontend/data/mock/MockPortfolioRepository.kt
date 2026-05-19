package com.example.rmp_frontend.data.mock

import com.example.rmp_frontend.domain.model.Portfolio
import com.example.rmp_frontend.domain.repository.PortfolioRepository
import kotlinx.coroutines.delay

class MockPortfolioRepository : PortfolioRepository {
    override suspend fun getPortfolio(): Portfolio {
        delay(250)
        return MockData.portfolio
    }
}
