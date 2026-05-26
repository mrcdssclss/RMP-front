package com.example.rmp_frontend.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.components.AppButton
import com.example.rmp_frontend.presentation.components.AppTextField
import com.example.rmp_frontend.presentation.components.AppTopBar
import com.example.rmp_frontend.presentation.components.EmptyView
import com.example.rmp_frontend.presentation.components.ErrorView
import com.example.rmp_frontend.presentation.components.LoadingView
import com.example.rmp_frontend.presentation.state.ProfileUiState
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onLogoutClick: () -> Unit,
    onUpdateCredentials: (String, String) -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showSettingsDialog by remember { mutableStateOf(false) }
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
        topBar = { AppTopBar(title = "Profile", onRefreshClick = handleRefreshClick) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        when (uiState) {
            ProfileUiState.Loading -> LoadingView(message = "Loading profile", modifier = Modifier.padding(padding))
            ProfileUiState.Empty -> EmptyView(
                title = "No profile",
                message = "Sign in to see profile details.",
                onRefreshClick = handleRefreshClick,
                modifier = Modifier.padding(padding)
            )
            is ProfileUiState.Error -> ErrorView(
                message = uiState.message,
                onRefreshClick = handleRefreshClick,
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
                    SettingsCard(
                        appVersion = uiState.appVersion,
                        onSettingsClick = { showSettingsDialog = true }
                    )
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

        val successState = uiState as? ProfileUiState.Success
        LaunchedEffect(successState?.credentialsEventId) {
            val message = successState?.credentialsMessage ?: successState?.credentialsError
            if (message != null) {
                snackbarHostState.showSnackbar(message)
            }
        }

        if (showSettingsDialog && successState != null) {
            SettingsDialog(
                initialLogin = successState.user.email,
                onSave = { login, password, repeatedPassword ->
                    if (password != repeatedPassword) {
                        showSnackbar("Passwords do not match")
                    } else {
                        onUpdateCredentials(login, password)
                        showSettingsDialog = false
                    }
                },
                onDismiss = { showSettingsDialog = false }
            )
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
private fun SettingsCard(
    appVersion: String,
    onSettingsClick: () -> Unit
) {
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
            AppButton(
                text = "Настройки",
                onClick = onSettingsClick,
                secondary = true
            )
        }
    }
}

@Composable
private fun SettingsDialog(
    initialLogin: String,
    onSave: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var login by remember(initialLogin) { mutableStateOf(initialLogin) }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Настройки") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = "Login / email"
                )
                AppTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "New password",
                    visualTransformation = PasswordVisualTransformation()
                )
                AppTextField(
                    value = repeatedPassword,
                    onValueChange = { repeatedPassword = it },
                    label = "Repeat password",
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(login, password, repeatedPassword) }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
