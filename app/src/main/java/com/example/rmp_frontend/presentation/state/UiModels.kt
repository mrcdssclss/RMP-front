package com.example.rmp_frontend.presentation.state

data class InstrumentSummaryUiModel(
    val ticker: String,
    val name: String,
    val price: Double,
    val changePercent: Double
)

data class InstrumentDetailsUiModel(
    val ticker: String,
    val name: String,
    val price: Double,
    val changePercent: Double,
    val chartPoints: List<Float>
)

data class PortfolioAssetUiModel(
    val ticker: String,
    val name: String,
    val quantity: Double,
    val positionValue: Double,
    val profitLoss: Double,
    val profitLossPercent: Double
)

enum class TransactionType {
    BUY,
    SELL
}

data class TransactionUiModel(
    val id: String,
    val date: String,
    val type: TransactionType,
    val ticker: String,
    val quantity: Double,
    val price: Double,
    val total: Double
)

data class UserProfileUiModel(
    val name: String,
    val email: String,
    val isAuthorized: Boolean
)
