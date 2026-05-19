package com.example.rmp_frontend.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rmp_frontend.presentation.state.PortfolioAssetUiModel

@Composable
fun PortfolioItemCard(
    asset: PortfolioAssetUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pnlColor = if (asset.profitLoss >= 0.0) Color(0xFF28C76F) else Color(0xFFFF5C5C)

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = asset.ticker, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${asset.name} - ${formatQuantity(asset.quantity)} pcs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = formatCurrency(asset.positionValue), style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${formatCurrency(asset.profitLoss)} (${formatPercent(asset.profitLossPercent)})",
                    color = pnlColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
