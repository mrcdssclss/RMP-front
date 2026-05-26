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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch

@Composable
fun InstrumentScreen(
    uiState: InstrumentUiState,
    onBackClick: () -> Unit,
    onPeriodSelected: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onBuyClick: (Double) -> Unit,
    onSellClick: (Double) -> Unit,
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
    val showSnackbar: (String) -> Unit = { message ->
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "Instrument",
                onBackClick = onBackClick,
                onRefreshClick = handleRefreshClick
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        when (uiState) {
            InstrumentUiState.Loading -> LoadingView(message = "Loading instrument", modifier = Modifier.padding(padding))
            InstrumentUiState.Empty -> EmptyView(
                title = "Instrument not found",
                message = "Select another instrument from the market list.",
                onRefreshClick = handleRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is InstrumentUiState.Error -> ErrorView(
                message = uiState.message,
                onRefreshClick = handleRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is InstrumentUiState.Success -> InstrumentContent(
                state = uiState,
                onPeriodSelected = onPeriodSelected,
                onQuantityChange = onQuantityChange,
                onBuyClick = onBuyClick,
                onSellClick = onSellClick,
                onOperationFeedback = showSnackbar,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun InstrumentContent(
    state: InstrumentUiState.Success,
    onPeriodSelected: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onBuyClick: (Double) -> Unit,
    onSellClick: (Double) -> Unit,
    onOperationFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var pendingOperation by remember { mutableStateOf<TradeOperation?>(null) }
    val parsedQuantity = state.quantity.toDoubleOrNull()
    val validQuantity = parsedQuantity != null && parsedQuantity > 0.0

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
                onPeriodSelected = onPeriodSelected
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
                    text = if (state.isOperationLoading) "Submitting..." else "Buy",
                    onClick = {
                        if (validQuantity) {
                            pendingOperation = TradeOperation.Buy
                        } else {
                            onBuyClick(0.0)
                            onOperationFeedback("Введите количество")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isOperationLoading,
                )
                AppButton(
                    text = "Sell",
                    onClick = {
                        if (validQuantity) {
                            pendingOperation = TradeOperation.Sell
                        } else {
                            onSellClick(0.0)
                            onOperationFeedback("Введите количество")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isOperationLoading,
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

    val operation = pendingOperation
    if (operation != null && parsedQuantity != null) {
        OperationConfirmationDialog(
            operation = operation,
            ticker = state.instrument.ticker,
            price = state.instrument.price,
            quantity = parsedQuantity,
            onConfirm = {
                pendingOperation = null
                when (operation) {
                    TradeOperation.Buy -> {
                        onBuyClick(parsedQuantity)
                    }
                    TradeOperation.Sell -> {
                        onSellClick(parsedQuantity)
                    }
                }
            },
            onDismiss = {
                pendingOperation = null
            }
        )
    }
}

private enum class TradeOperation {
    Buy,
    Sell
}

@Composable
private fun OperationConfirmationDialog(
    operation: TradeOperation,
    ticker: String,
    price: Double,
    quantity: Double,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val total = price * quantity
    val operationTitle = when (operation) {
        TradeOperation.Buy -> "Confirm buy"
        TradeOperation.Sell -> "Confirm sell"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(operationTitle) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Ticker: $ticker")
                Text(text = "Price: ${formatCurrency(price)}")
                Text(text = "Quantity: $quantity")
                Text(text = "Total: ${formatCurrency(total)}")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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
    onPeriodSelected: (String) -> Unit
) {
    val periods = listOf("1D", "1W", "1M", "1Y", "ALL")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        periods.forEach { period ->
            val selected = period == selectedPeriod
            OutlinedButton(
                onClick = { onPeriodSelected(period) },
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    contentColor = if (selected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            ) {
                Text(text = period)
            }
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
}
