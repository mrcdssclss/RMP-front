package com.example.rmp_frontend.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.rmp_frontend.presentation.viewmodel.AuthViewModel
import com.example.rmp_frontend.presentation.viewmodel.HistoryViewModel
import com.example.rmp_frontend.presentation.viewmodel.InstrumentViewModel
import com.example.rmp_frontend.presentation.viewmodel.MarketViewModel
import com.example.rmp_frontend.presentation.viewmodel.PortfolioViewModel
import com.example.rmp_frontend.presentation.viewmodel.ProfileViewModel

class ViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(appContainer.authRepository)
            }
            modelClass.isAssignableFrom(MarketViewModel::class.java) -> {
                MarketViewModel(appContainer.marketRepository)
            }
            modelClass.isAssignableFrom(InstrumentViewModel::class.java) -> {
                InstrumentViewModel(appContainer.instrumentRepository, appContainer.tradingRepository)
            }
            modelClass.isAssignableFrom(PortfolioViewModel::class.java) -> {
                PortfolioViewModel(appContainer.portfolioRepository)
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(appContainer.historyRepository)
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(appContainer.profileRepository, appContainer.authRepository)
            }
            else -> error("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }
}
