package com.currency.app.data.remote

import com.currency.app.BuildConfig // 
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {
    @GET("v6/{apiKey}/latest/USD")
    suspend fun getLiveExchangeRates(
        @Path("apiKey") apiKey: String = BuildConfig.EXCHANGE_API_KEY
    ): CurrencyResponseDto
}

data class CurrencyResponseDto(
    val result: String,
    val conversion_rates: Map<String, Double>
)