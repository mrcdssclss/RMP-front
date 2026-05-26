package com.example.rmp_frontend.presentation.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.components.ErrorView
import com.example.rmp_frontend.presentation.state.SplashUiState

@Composable
fun SplashScreen(
    uiState: SplashUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        SplashUiState.Loading,
        SplashUiState.NavigateToAuth,
        SplashUiState.NavigateToMain -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "RMP Invest",
                    style = MaterialTheme.typography.headlineLarge
                )
                CircularProgressIndicator()
            }
        }

        is SplashUiState.Error -> ErrorView(message = uiState.message, modifier = modifier)
    }
}
