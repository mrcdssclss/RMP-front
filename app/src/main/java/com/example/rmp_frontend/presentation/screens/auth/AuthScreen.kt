package com.example.rmp_frontend.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.components.LoadingView
import com.example.rmp_frontend.presentation.state.AuthUiState

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginModeClick: () -> Unit,
    onRegisterModeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val form = uiState.toForm()
    val isLoading = uiState is AuthUiState.Loading

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RMP Invest",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Trading terminal",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            LoadingView(message = "Submitting", modifier = Modifier.height(180.dp))
        }

        if (form.isRegisterMode) {
            RegisterScreen(
                name = form.name,
                email = form.email,
                password = form.password,
                isLoading = isLoading,
                emailError = form.emailError,
                passwordError = form.passwordError,
                onNameChange = onNameChange,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onRegisterClick = onRegisterClick,
                onLoginModeClick = onLoginModeClick
            )
        } else {
            LoginScreen(
                email = form.email,
                password = form.password,
                isLoading = isLoading,
                emailError = form.emailError,
                passwordError = form.passwordError,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = onLoginClick,
                onRegisterModeClick = onRegisterModeClick
            )
        }

        if (uiState is AuthUiState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private data class AuthForm(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isRegisterMode: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)

private fun AuthUiState.toForm(): AuthForm {
    return when (this) {
        AuthUiState.Empty,
        AuthUiState.Success -> AuthForm()
        is AuthUiState.Editing -> AuthForm(email, password, name, isRegisterMode, emailError, passwordError)
        is AuthUiState.Error -> AuthForm(email, password, name, isRegisterMode)
        is AuthUiState.Loading -> AuthForm(email, password, name, isRegisterMode)
    }
}
