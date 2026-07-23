package com.currency.app.di

import com.currency.app.BuildConfig
import com.currency.app.data.remote.CryptoApiService
import com.currency.app.data.remote.CurrencyApiService
import com.currency.app.data.remote.StockApiService
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideCryptoApiService(okHttpClient: OkHttpClient): CryptoApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStockApiService(okHttpClient: OkHttpClient): StockApiService {
        return Retrofit.Builder()
            .baseUrl("https://finnhub.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StockApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyApiService(okHttpClient: OkHttpClient): CurrencyApiService {
        return Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
    }

    // 🤖 ለ Gemini AI Assistant የሚያስፈልገው Hilt Provider (ከ BuildConfig ቁልፍን የሚወስድ)
    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }
}