package com.currency.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApiService {
    @GET("api/v3/coins/markets")
    suspend fun getCryptoMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50, // 🚀 የኮይኖቹን ብዛት ወደ 50 ከፍ አድርገነዋል
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = true
    ): List<CryptoMarketDto>
}

data class CryptoMarketDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: Double, // ⚠️ በሰርቨሩ ስም መሠረት መሆን አለበት
    val price_change_percentage_24h: Double,
    val sparkline_in_7d: SparklineIn7d?
)

data class SparklineIn7d(
    val price: List<Double>
)