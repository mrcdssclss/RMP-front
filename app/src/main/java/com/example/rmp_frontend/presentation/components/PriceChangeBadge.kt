package com.example.rmp_frontend.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val PositiveColor = Color(0xFF28C76F)
private val NegativeColor = Color(0xFFFF5C5C)

@Composable
fun PriceChangeBadge(
    changePercent: Double,
    modifier: Modifier = Modifier
) {
    val color = if (changePercent >= 0.0) PositiveColor else NegativeColor
    Text(
        text = formatPercent(changePercent),
        modifier = modifier
            .background(color.copy(alpha = 0.16f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = color,
        style = MaterialTheme.typography.labelMedium
    )
}
