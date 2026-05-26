package com.example.rmp_frontend.presentation.state

import com.example.rmp_frontend.domain.model.PortfolioItem

data class PortfolioUiState(
    val isLoading: Boolean = false,
    val balance: Double = 0.0,
    val currency: String = "RUB",
    val assets: List<PortfolioItem> = emptyList(),
    val totalValue: Double = 0.0,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
)
