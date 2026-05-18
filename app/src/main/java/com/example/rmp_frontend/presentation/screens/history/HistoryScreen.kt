package com.example.rmp_frontend.presentation.screens.history

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
import com.example.rmp_frontend.presentation.components.LoadingView
import com.example.rmp_frontend.presentation.components.TransactionItem
import com.example.rmp_frontend.presentation.state.HistoryUiState

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { AppTopBar(title = "History", onRefreshClick = onRefreshClick) }
    ) { padding ->
        when (uiState) {
            HistoryUiState.Loading -> LoadingView(message = "Loading history", modifier = Modifier.padding(padding))
            HistoryUiState.Empty -> EmptyView(
                title = "No operations",
                message = "Completed operations will appear here.",
                onRefreshClick = onRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is HistoryUiState.Error -> ErrorView(
                message = uiState.message,
                onRefreshClick = onRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is HistoryUiState.Success -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.transactions, key = { it.id }) { transaction ->
                    TransactionItem(transaction = transaction)
                }
            }
        }
    }
}
