package com.example.rmp_frontend.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PortfolioHeader(
    totalBalance: Double,
    assetsCount: Int
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = "Portfolio value",
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = "$${"%.2f".format(totalBalance)}",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$assetsCount assets in portfolio",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}