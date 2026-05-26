package com.example.rmp_frontend.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rmp_frontend.presentation.screens.auth.AuthScreen
import com.example.rmp_frontend.presentation.screens.history.HistoryScreen
import com.example.rmp_frontend.presentation.screens.instrument.InstrumentScreen
import com.example.rmp_frontend.presentation.screens.market.MarketScreen
import com.example.rmp_frontend.presentation.screens.portfolio.PortfolioScreen
import com.example.rmp_frontend.presentation.screens.profile.ProfileScreen
import com.example.rmp_frontend.presentation.screens.splash.SplashScreen
import com.example.rmp_frontend.presentation.di.AppContainer
import com.example.rmp_frontend.presentation.di.ViewModelFactory
import com.example.rmp_frontend.presentation.state.AuthUiState
import com.example.rmp_frontend.presentation.state.ProfileUiState
import com.example.rmp_frontend.presentation.state.SplashUiState
import com.example.rmp_frontend.presentation.viewmodel.AuthViewModel
import com.example.rmp_frontend.presentation.viewmodel.HistoryViewModel
import com.example.rmp_frontend.presentation.viewmodel.InstrumentViewModel
import com.example.rmp_frontend.presentation.viewmodel.MarketViewModel
import com.example.rmp_frontend.presentation.viewmodel.PortfolioViewModel
import com.example.rmp_frontend.presentation.viewmodel.ProfileViewModel
import com.example.rmp_frontend.presentation.viewmodel.SplashViewModel

@Composable
fun AppNavGraph(appContainer: AppContainer) {
    val navController = rememberNavController()
    val factory = remember(appContainer) { ViewModelFactory(appContainer) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = bottomBarScreens.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Splash.route) {
                val viewModel: SplashViewModel = viewModel(factory = factory)
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(uiState) {
                    when (uiState) {
                        SplashUiState.NavigateToAuth -> navController.navigate(Screen.Auth.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                        SplashUiState.NavigateToMain -> navController.navigate(Screen.Market.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                        else -> Unit
                    }
                }

                SplashScreen(uiState = uiState)
            }

            composable(Screen.Auth.route) {
                val viewModel: AuthViewModel = viewModel(factory = factory)
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(uiState) {
                    if (uiState is AuthUiState.Success) {
                        navController.navigate(Screen.Market.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                        viewModel.clearAuthResult()
                    }
                }

                AuthScreen(
                    uiState = uiState,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onNameChange = viewModel::onNameChange,
                    onLoginClick = viewModel::onLoginClick,
                    onRegisterClick = viewModel::onRegisterClick,
                    onLoginModeClick = viewModel::showLogin,
                    onRegisterModeClick = viewModel::showRegister
                )
            }

            composable(Screen.Market.route) {
                val viewModel: MarketViewModel = viewModel(factory = factory)
                val uiState by viewModel.uiState.collectAsState()

                MarketScreen(
                    uiState = uiState,
                    onInstrumentClick = { ticker ->
                        navController.navigate(Screen.Instrument.createRoute(ticker))
                    },
                    onRefreshClick = viewModel::onRefreshClick
                )
            }

            composable(
                route = Screen.Instrument.route,
                arguments = listOf(navArgument("ticker") { type = NavType.StringType })
            ) { entry ->
                val ticker = entry.arguments?.getString("ticker").orEmpty()
                val viewModel: InstrumentViewModel = viewModel(factory = factory)
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(ticker) {
                    viewModel.loadInstrument(ticker)
                }

                InstrumentScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onPeriodSelected = viewModel::onPeriodSelected,
                    onQuantityChange = viewModel::onQuantityChange,
                    onBuyClick = viewModel::onBuyClick,
                    onSellClick = viewModel::onSellClick,
                    onRefreshClick = viewModel::onRefreshClick
                )
            }

            composable(Screen.Portfolio.route) {
                val viewModel: PortfolioViewModel = viewModel(factory = factory)
                val uiState by viewModel.uiState.collectAsState()

                PortfolioScreen(
                    uiState = uiState,
                    onPortfolioInstrumentClick = { ticker ->
                        navController.navigate(Screen.Instrument.createRoute(ticker))
                    },
                    onRefreshClick = viewModel::onRefreshClick
                )
            }

            composable(Screen.History.route) {
                val viewModel: HistoryViewModel = viewModel(factory = factory)
                val uiState by viewModel.uiState.collectAsState()

                HistoryScreen(
                    uiState = uiState,
                    onRefreshClick = viewModel::onRefreshClick
                )
            }

            composable(Screen.Profile.route) {
                val viewModel: ProfileViewModel = viewModel(factory = factory)
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(uiState) {
                    val state = uiState as? ProfileUiState.Success
                    if (state != null && !state.user.isAuthorized) {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(Screen.Market.route) { inclusive = true }
                        }
                    }
                }

                ProfileScreen(
                    uiState = uiState,
                    onLogoutClick = viewModel::onLogoutClick,
                    onUpdateProfile = viewModel::onUpdateProfile,
                    onRefreshClick = viewModel::onRefreshClick
                )
            }
        }
    }
}
