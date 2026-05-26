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
import com.example.rmp_frontend.presentation.state.TransactionType
import com.example.rmp_frontend.presentation.state.TransactionUiModel

@Composable
fun TransactionItem(
    transaction: TransactionUiModel,
    modifier: Modifier = Modifier
) {
    val typeColor = if (transaction.type == TransactionType.BUY) Color(0xFF28C76F) else Color(0xFFFF5C5C)

    Card(
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
                Text(
                    text = "${transaction.type} ${transaction.ticker}",
                    color = typeColor,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${transaction.date} - ${formatQuantity(transaction.quantity)} pcs at ${formatCurrency(transaction.price)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = formatCurrency(transaction.total),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
