package com.example.rmp_frontend.presentation.screens.market

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.components.AppTopBar
import com.example.rmp_frontend.presentation.components.EmptyView
import com.example.rmp_frontend.presentation.components.ErrorView
import com.example.rmp_frontend.presentation.components.InstrumentCard
import com.example.rmp_frontend.presentation.components.LoadingView
import com.example.rmp_frontend.presentation.state.MarketUiState

@Composable
fun MarketScreen(
    uiState: MarketUiState,
    onInstrumentClick: (String) -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { AppTopBar(title = "Market", onRefreshClick = onRefreshClick) }
    ) { padding ->
        when (uiState) {
            MarketUiState.Loading -> LoadingView(message = "Loading market", modifier = Modifier.padding(padding))
            MarketUiState.Empty -> EmptyView(
                title = "No instruments",
                message = "The market list is empty.",
                onRefreshClick = onRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is MarketUiState.Error -> ErrorView(
                message = uiState.message,
                onRefreshClick = onRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is MarketUiState.Success -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.instruments, key = { it.ticker }) { instrument ->
                    InstrumentCard(
                        instrument = instrument,
                        onClick = { onInstrumentClick(instrument.ticker) }
                    )
                }
            }
        }
    }
}
