package com.currency.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a financial asset table in the local database.
 */
@Entity(tableName = "market_items")
data class MarketEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val assetType: String, // Stored as String (CURRENCY, STOCK, CRYPTO)
    val iconUrl: String? = null,
    val localBankName: String? = null
)