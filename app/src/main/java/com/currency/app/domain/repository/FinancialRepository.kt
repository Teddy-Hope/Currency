package com.currency.app.domain.repository

import com.currency.app.domain.model.CryptoItem
import com.currency.app.domain.model.CurrencyItem
import com.currency.app.domain.model.StockItem
import kotlinx.coroutines.flow.Flow

interface FinancialRepository {
    fun getLiveExchangeRates(): Flow<List<CurrencyItem>>
    fun getCryptoMarkets(): Flow<List<CryptoItem>>
    fun getTopStocks(): Flow<List<StockItem>>
}