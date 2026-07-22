package com.currency.app.data.local

import android.content.Context
import androidx.room.Room
import com.currency.app.BuildConfig
import com.currency.app.data.remote.CryptoApiService
import com.currency.app.data.remote.CurrencyApiService
import com.currency.app.data.remote.StockApiService
import com.currency.app.data.repository.FinancialRepositoryImpl
import com.currency.app.domain.repository.FinancialRepository
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "currency_markets_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideMarketDao(
        appDatabase: AppDatabase
    ): MarketDao {
        return appDatabase.marketDao()
    }

    @Provides
    @Singleton
    fun provideFinancialRepository(
        cryptoApi: CryptoApiService,
        stockApi: StockApiService,
        currencyApi: CurrencyApiService
    ): FinancialRepository {
        return FinancialRepositoryImpl(cryptoApi, stockApi, currencyApi)
    }

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        // 🚀 እውነተኛውን የጂሚኒ ቁልፍ በቀጥታ ከ BuildConfig እንወስዳለን (ዜሮ ዴሞ/ውሸት የለም)
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }
}