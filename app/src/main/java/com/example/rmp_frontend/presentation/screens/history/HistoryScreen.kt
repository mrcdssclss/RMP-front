package com.example.rmp_frontend.presentation.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.components.AppTopBar
import com.example.rmp_frontend.presentation.components.EmptyView
import com.example.rmp_frontend.presentation.components.ErrorView
import com.example.rmp_frontend.presentation.components.LoadingView
import com.example.rmp_frontend.presentation.components.TransactionItem
import com.example.rmp_frontend.presentation.state.HistoryUiState
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
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
        topBar = { AppTopBar(title = "History", onRefreshClick = handleRefreshClick) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        when (uiState) {
            HistoryUiState.Loading -> LoadingView(message = "Loading history", modifier = Modifier.padding(padding))
            HistoryUiState.Empty -> EmptyView(
                title = "No operations",
                message = "Completed operations will appear here.",
                onRefreshClick = handleRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is HistoryUiState.Error -> ErrorView(
                message = uiState.message,
                onRefreshClick = handleRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is HistoryUiState.Success -> {
                if (uiState.transactions.isEmpty()) {
                    EmptyView(
                        title = "No operations",
                        message = "Completed operations will appear here.",
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
                        items(uiState.transactions, key = { it.id }) { transaction ->
                            TransactionItem(transaction = transaction)
                        }
                    }
                }
            }
        }
    }
}
