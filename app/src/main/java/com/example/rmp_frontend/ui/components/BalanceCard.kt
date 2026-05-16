package com.example.rmp_frontend.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BalanceCard(
    balance: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Total portfolio",
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = balance,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Your investments",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}