package com.example.rmp_frontend.presentation.di

import android.content.Context
import com.example.rmp_frontend.data.api.ApiClient
import com.example.rmp_frontend.data.mock.MockAuthRepository
import com.example.rmp_frontend.data.mock.MockHistoryRepository
import com.example.rmp_frontend.data.mock.MockInstrumentRepository
import com.example.rmp_frontend.data.mock.MockMarketRepository
import com.example.rmp_frontend.data.mock.MockPortfolioRepository
import com.example.rmp_frontend.data.mock.MockProfileRepository
import com.example.rmp_frontend.data.mock.MockTradingRepository
import com.example.rmp_frontend.data.repository.AuthRepositoryImpl
import com.example.rmp_frontend.data.repository.HistoryRepositoryImpl
import com.example.rmp_frontend.data.repository.InstrumentRepositoryImpl
import com.example.rmp_frontend.data.repository.MarketRepositoryImpl
import com.example.rmp_frontend.data.repository.PortfolioRepositoryImpl
import com.example.rmp_frontend.data.repository.ProfileRepositoryImpl
import com.example.rmp_frontend.data.repository.TradingRepositoryImpl
import com.example.rmp_frontend.data.storage.TokenStorage
import com.example.rmp_frontend.data.websocket.PriceWebSocketClient
import com.example.rmp_frontend.domain.repository.AuthRepository
import com.example.rmp_frontend.domain.repository.HistoryRepository
import com.example.rmp_frontend.domain.repository.InstrumentRepository
import com.example.rmp_frontend.domain.repository.MarketRepository
import com.example.rmp_frontend.domain.repository.PortfolioRepository
import com.example.rmp_frontend.domain.repository.ProfileRepository
import com.example.rmp_frontend.domain.repository.TradingRepository

class AppContainer(
    context: Context,
    useMock: Boolean = true,
    baseUrl: String = "https://example.com/api/",
    webSocketUrl: String = "wss://example.com/quotes",
) {
    private val appContext = context.applicationContext
    val tokenStorage = TokenStorage(appContext)

    private val apiClient by lazy { ApiClient(baseUrl, tokenStorage) }
    private val priceWebSocketClient by lazy { PriceWebSocketClient(webSocketUrl, tokenStorage) }

    val authRepository: AuthRepository by lazy {
        if (useMock) MockAuthRepository(tokenStorage) else AuthRepositoryImpl(apiClient.authApi, tokenStorage)
    }

    val marketRepository: MarketRepository by lazy {
        if (useMock) MockMarketRepository() else MarketRepositoryImpl(apiClient.marketApi, priceWebSocketClient)
    }

    val instrumentRepository: InstrumentRepository by lazy {
        if (useMock) MockInstrumentRepository() else InstrumentRepositoryImpl(apiClient.instrumentApi)
    }

    val tradingRepository: TradingRepository by lazy {
        if (useMock) MockTradingRepository() else TradingRepositoryImpl(apiClient.tradingApi)
    }

    val portfolioRepository: PortfolioRepository by lazy {
        if (useMock) MockPortfolioRepository() else PortfolioRepositoryImpl(apiClient.portfolioApi)
    }

    val historyRepository: HistoryRepository by lazy {
        if (useMock) MockHistoryRepository() else HistoryRepositoryImpl(apiClient.historyApi)
    }

    val profileRepository: ProfileRepository by lazy {
        if (useMock) MockProfileRepository() else ProfileRepositoryImpl(apiClient.profileApi)
    }
}
