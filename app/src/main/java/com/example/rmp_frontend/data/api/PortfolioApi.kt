package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.PortfolioDto
import retrofit2.http.GET

interface PortfolioApi {
    @GET("portfolio")
    suspend fun getPortfolio(): PortfolioDto
}
