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
fun RegisterScreen(
    name: String,
    email: String,
    password: String,
    isLoading: Boolean,
    emailError: String?,
    passwordError: String?,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginModeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AppTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Name",
            enabled = !isLoading
        )
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
            text = if (isLoading) "Creating account" else "Register",
            onClick = onRegisterClick,
            enabled = !isLoading
        )
        AppButton(
            text = "I already have an account",
            onClick = onLoginModeClick,
            enabled = !isLoading,
            secondary = true
        )
    }
}
