package com.currency.app.domain.model

data class CurrencyItem(
    val bankName: String,
    val bankCode: String,
    val buyPrice: Double,
    val sellPrice: Double,
    val flagUrl: String // 
)