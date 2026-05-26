package com.example.rmp_frontend.presentation.screens.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.components.AppTopBar
import com.example.rmp_frontend.presentation.components.EmptyView
import com.example.rmp_frontend.presentation.components.ErrorView
import com.example.rmp_frontend.presentation.components.LoadingView
import com.example.rmp_frontend.presentation.components.PortfolioItemCard
import com.example.rmp_frontend.presentation.components.formatCurrency
import com.example.rmp_frontend.presentation.state.PortfolioUiState
import kotlinx.coroutines.launch

@Composable
fun PortfolioScreen(
    uiState: PortfolioUiState,
    onPortfolioInstrumentClick: (String) -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val handleRefreshClick: () -> Unit = {
        onRefreshClick()
        scope.launch {
            snackbarHostState.showSnackbar("Данные обновляются")
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { AppTopBar(title = "Portfolio", onRefreshClick = handleRefreshClick) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        when (uiState) {
            PortfolioUiState.Loading -> LoadingView(message = "Loading portfolio", modifier = Modifier.padding(padding))
            PortfolioUiState.Empty -> EmptyView(
                title = "No assets",
                message = "Portfolio positions will appear here.",
                onRefreshClick = handleRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is PortfolioUiState.Error -> ErrorView(
                message = uiState.message,
                onRefreshClick = handleRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is PortfolioUiState.Success -> {
                if (uiState.assets.isEmpty()) {
                    EmptyView(
                        title = "No assets",
                        message = "Portfolio positions will appear here.",
                        onRefreshClick = handleRefreshClick,
                        modifier = Modifier.padding(padding)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            PortfolioSummary(
                                cashBalance = uiState.cashBalance,
                                totalValue = uiState.totalValue
                            )
                        }
                        items(uiState.assets, key = { it.ticker }) { asset ->
                            PortfolioItemCard(
                                asset = asset,
                                onClick = { onPortfolioInstrumentClick(asset.ticker) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PortfolioSummary(
    cashBalance: Double,
    totalValue: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Total portfolio value",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = formatCurrency(totalValue),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Cash balance: ${formatCurrency(cashBalance)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
