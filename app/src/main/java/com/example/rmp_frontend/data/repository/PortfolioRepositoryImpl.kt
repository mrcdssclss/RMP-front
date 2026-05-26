package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.PortfolioApi
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.domain.model.Portfolio
import com.example.rmp_frontend.domain.repository.PortfolioRepository

class PortfolioRepositoryImpl(
    private val portfolioApi: PortfolioApi,
) : PortfolioRepository {
    override suspend fun getPortfolio(): Portfolio {
        return portfolioApi.getPortfolio().toDomain()
    }
}
