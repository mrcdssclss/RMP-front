package com.example.rmp_frontend.presentation.screens.instrument

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.components.AppButton
import com.example.rmp_frontend.presentation.components.AppTextField
import com.example.rmp_frontend.presentation.components.AppTopBar
import com.example.rmp_frontend.presentation.components.EmptyView
import com.example.rmp_frontend.presentation.components.ErrorView
import com.example.rmp_frontend.presentation.components.LoadingView
import com.example.rmp_frontend.presentation.components.PriceChangeBadge
import com.example.rmp_frontend.presentation.components.formatCurrency
import com.example.rmp_frontend.presentation.state.InstrumentUiState

@Composable
fun InstrumentScreen(
    uiState: InstrumentUiState,
    onBackClick: () -> Unit,
    onPeriodClick: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onBuyClick: () -> Unit,
    onSellClick: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "Instrument",
                onBackClick = onBackClick,
                onRefreshClick = onRefreshClick
            )
        }
    ) { padding ->
        when (uiState) {
            InstrumentUiState.Loading -> LoadingView(message = "Loading instrument", modifier = Modifier.padding(padding))
            InstrumentUiState.Empty -> EmptyView(
                title = "Instrument not found",
                message = "Select another instrument from the market list.",
                onRefreshClick = onRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is InstrumentUiState.Error -> ErrorView(
                message = uiState.message,
                onRefreshClick = onRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is InstrumentUiState.Success -> InstrumentContent(
                state = uiState,
                onPeriodClick = onPeriodClick,
                onQuantityChange = onQuantityChange,
                onBuyClick = onBuyClick,
                onSellClick = onSellClick,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun InstrumentContent(
    state: InstrumentUiState.Success,
    onPeriodClick: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onBuyClick: () -> Unit,
    onSellClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = state.instrument.ticker, style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = state.instrument.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(text = formatCurrency(state.instrument.price), style = MaterialTheme.typography.headlineMedium)
                PriceChangeBadge(changePercent = state.instrument.changePercent)
            }
        }

        item {
            PriceChart(points = state.instrument.chartPoints)
        }

        item {
            PeriodSelector(
                selectedPeriod = state.selectedPeriod,
                onPeriodClick = onPeriodClick
            )
        }

        item {
            AppTextField(
                value = state.quantity,
                onValueChange = onQuantityChange,
                label = "Quantity"
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppButton(
                    text = "Buy",
                    onClick = onBuyClick,
                    modifier = Modifier.weight(1f)
                )
                AppButton(
                    text = "Sell",
                    onClick = onSellClick,
                    modifier = Modifier.weight(1f),
                    secondary = true
                )
            }
        }

        if (state.operationMessage != null || state.operationError != null) {
            item {
                Text(
                    text = state.operationMessage ?: state.operationError.orEmpty(),
                    color = if (state.operationError == null) Color(0xFF28C76F) else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun PriceChart(points: List<Float>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            if (points.size < 2) return@Canvas

            val min = points.minOrNull() ?: 0f
            val max = points.maxOrNull() ?: 1f
            val range = (max - min).takeIf { it != 0f } ?: 1f
            val stepX = size.width / (points.lastIndex)
            val chartColor = Color(0xFF28C76F)

            points.zipWithNext().forEachIndexed { index, (start, end) ->
                val startY = size.height - ((start - min) / range * size.height)
                val endY = size.height - ((end - min) / range * size.height)
                drawLine(
                    color = chartColor,
                    start = Offset(index * stepX, startY),
                    end = Offset((index + 1) * stepX, endY),
                    strokeWidth = 6f
                )
            }
        }
    }
}

@Composable
private fun PeriodSelector(
    selectedPeriod: String,
    onPeriodClick: (String) -> Unit
) {
    val periods = listOf("1D", "1W", "1M", "1Y")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        periods.forEach { period ->
            OutlinedButton(
                onClick = { onPeriodClick(period) },
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = period,
                    color = if (period == selectedPeriod) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
}
