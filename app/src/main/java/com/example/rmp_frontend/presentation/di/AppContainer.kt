package com.example.rmp_frontend.presentation.di

import android.content.Context
import com.example.rmp_frontend.data.api.ApiClient
import com.example.rmp_frontend.data.api.ApiConfig
import com.example.rmp_frontend.data.mock.MockPortfolioRepository
import com.example.rmp_frontend.data.repository.AuthRepositoryImpl
import com.example.rmp_frontend.data.repository.HistoryRepositoryImpl
import com.example.rmp_frontend.data.repository.InstrumentRepositoryImpl
import com.example.rmp_frontend.data.repository.MarketRepositoryImpl
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
    baseUrl: String = ApiConfig.BASE_URL,
    webSocketUrl: String = ApiConfig.MARKET_WEBSOCKET_URL,
) {
    private val appContext = context.applicationContext
    val tokenStorage = TokenStorage(appContext)

    private val apiClient by lazy { ApiClient(tokenStorage, baseUrl) }
    private val priceWebSocketClient by lazy { PriceWebSocketClient(webSocketUrl, tokenStorage) }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(apiClient.authApi, tokenStorage)
    }

    val marketRepository: MarketRepository by lazy {
        MarketRepositoryImpl(apiClient.marketApi, priceWebSocketClient)
    }

    val instrumentRepository: InstrumentRepository by lazy {
        InstrumentRepositoryImpl(apiClient.instrumentApi)
    }

    val tradingRepository: TradingRepository by lazy {
        TradingRepositoryImpl(apiClient.tradingApi)
    }

    val portfolioRepository: PortfolioRepository by lazy {
        MockPortfolioRepository(marketRepository)
    }

    val historyRepository: HistoryRepository by lazy {
        HistoryRepositoryImpl(apiClient.historyApi)
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(apiClient.profileApi)
    }
}
