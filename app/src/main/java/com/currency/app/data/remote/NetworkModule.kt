package com.currency.app.di

import com.currency.app.data.remote.CryptoApiService
import com.currency.app.data.remote.CurrencyApiService
import com.currency.app.data.remote.StockApiService
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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/") 
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCryptoApiService(retrofit: Retrofit): CryptoApiService {
        return retrofit.create(CryptoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStockApiService(retrofit: Retrofit): StockApiService {
        // 🚀 የ IEX Sandboxን ሰብረን ወደ እውነተኛው የቀጥታ የፋይናንስ ሰርቨር (Finnhub Production Engine) መለወጫ መስመር
        return retrofit.newBuilder()
            .baseUrl("https://finnhub.io/")
            .build()
            .create(StockApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyApiService(retrofit: Retrofit): CurrencyApiService {
        // 🚀 የምንዛሬውን ቤዝ ዩአርኤል ወደ እውነተኛው የ v6 ፍሰት ማስተካከያ
        return retrofit.newBuilder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .build()
            .create(CurrencyApiService::class.java)
    }
}