package com.example.rmp_frontend.presentation.components

import java.util.Locale

fun formatCurrency(value: Double): String {
    return "$" + String.format(Locale.US, "%,.2f", value)
}

fun formatQuantity(value: Double): String {
    return String.format(Locale.US, "%,.4f", value).trimEnd('0').trimEnd('.')
}

fun formatPercent(value: Double): String {
    val sign = if (value > 0.0) "+" else ""
    return sign + String.format(Locale.US, "%.2f", value) + "%"
}
