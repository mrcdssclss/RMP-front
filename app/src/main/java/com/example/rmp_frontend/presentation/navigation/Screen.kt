package com.example.rmp_frontend.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Auth : Screen("auth")
    data object Market : Screen("market")
    data object Portfolio : Screen("portfolio")
    data object History : Screen("history")
    data object Profile : Screen("profile")
    data object Instrument : Screen("instrument/{ticker}") {
        fun createRoute(ticker: String): String = "instrument/${Uri.encode(ticker)}"
    }
}

val bottomBarScreens = listOf(
    Screen.Market,
    Screen.Portfolio,
    Screen.History,
    Screen.Profile
)
