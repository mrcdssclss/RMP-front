package com.example.rmp_frontend.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.components.AppButton
import com.example.rmp_frontend.presentation.components.AppTopBar
import com.example.rmp_frontend.presentation.components.EmptyView
import com.example.rmp_frontend.presentation.components.ErrorView
import com.example.rmp_frontend.presentation.components.LoadingView
import com.example.rmp_frontend.presentation.state.ProfileUiState

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onLogoutClick: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { AppTopBar(title = "Profile", onRefreshClick = onRefreshClick) }
    ) { padding ->
        when (uiState) {
            ProfileUiState.Loading -> LoadingView(message = "Loading profile", modifier = Modifier.padding(padding))
            ProfileUiState.Empty -> EmptyView(
                title = "No profile",
                message = "Sign in to see profile details.",
                onRefreshClick = onRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is ProfileUiState.Error -> ErrorView(
                message = uiState.message,
                onRefreshClick = onRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is ProfileUiState.Success -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ProfileCard(uiState = uiState)
                }
                item {
                    SettingsCard(appVersion = uiState.appVersion)
                }
                item {
                    AppButton(
                        text = "Logout",
                        onClick = onLogoutClick,
                        secondary = true
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(uiState: ProfileUiState.Success) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = uiState.user.name, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = uiState.user.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (uiState.user.isAuthorized) "Authorized" else "Not authorized",
                style = MaterialTheme.typography.bodyMedium,
                color = if (uiState.user.isAuthorized) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun SettingsCard(appVersion: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Settings", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Theme: dark",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "App version: $appVersion",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
