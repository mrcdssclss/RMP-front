package com.example.rmp_frontend.domain.repository

import com.example.rmp_frontend.domain.model.Portfolio

interface PortfolioRepository {
    suspend fun getPortfolio(): Portfolio
}
