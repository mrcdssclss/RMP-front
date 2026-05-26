package com.example.rmp_frontend.model

data class Asset(
    val name: String,
    val price: Double,
    val change: Double,
    val ownedQuantity: Double,
    val avgBuyPrice: Double
)