package com.currency.app.domain.model

data class CryptoItem(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val sparklineData: List<Float>, // 
    val iconUrl: String
)