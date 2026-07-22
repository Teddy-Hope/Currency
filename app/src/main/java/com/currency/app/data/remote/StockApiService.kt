package com.currency.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiService {
    @GET("api/v1/quote")
    suspend fun getStockQuote(
        @Query("symbol") symbol: String,
        @Query("token") token: String = BuildConfig.FINNHUB_API_KEY // 
    ): StockQuoteDto
}
data class StockQuoteDto(
    val c: Double,  // Current price 
    val dp: Double  // Percentage change 
)package com.currency.app.data.remote

import com.currency.app.BuildConfig // 
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiService {
    @GET("api/v1/quote")
    suspend fun getStockQuote(
        @Query("symbol") symbol: String,
        @Query("token") token: String = BuildConfig.FINNHUB_API_KEY
    ): StockQuoteDto
}

data class StockQuoteDto(
    val c: Double,  // Current price
    val dp: Double  // Percentage change
)