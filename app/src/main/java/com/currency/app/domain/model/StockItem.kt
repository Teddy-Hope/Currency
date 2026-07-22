package com.currency.app.domain.model

data class StockItem(
    val symbol: String,
    val name: String,
    val price: Double,
    val changePercent: Double,
    val sparklineData: List<Float>, // 
    val imageUrl: String
)