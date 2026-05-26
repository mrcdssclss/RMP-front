package com.example.rmp_frontend.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.components.AppButton
import com.example.rmp_frontend.presentation.components.AppTextField

@Composable
fun LoginScreen(
    email: String,
    password: String,
    isLoading: Boolean,
    emailError: String?,
    passwordError: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterModeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AppTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            enabled = !isLoading,
            error = emailError
        )
        AppTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            enabled = !isLoading,
            error = passwordError,
            visualTransformation = PasswordVisualTransformation()
        )
        AppButton(
            text = if (isLoading) "Signing in" else "Sign in",
            onClick = onLoginClick,
            enabled = !isLoading
        )
        AppButton(
            text = "Create account",
            onClick = onRegisterModeClick,
            enabled = !isLoading,
            secondary = true
        )
    }
}
