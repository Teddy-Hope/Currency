package com.currency.app.data.repository

import com.currency.app.data.remote.CryptoApiService
import com.currency.app.data.remote.StockApiService
import com.currency.app.data.remote.CurrencyApiService
import com.currency.app.domain.model.CryptoItem
import com.currency.app.domain.model.CurrencyItem
import com.currency.app.domain.model.StockItem
import com.currency.app.domain.repository.FinancialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FinancialRepositoryImpl @Inject constructor(
    private val cryptoApi: CryptoApiService,
    private val stockApi: StockApiService,
    private val currencyApi: CurrencyApiService
) : FinancialRepository {

    // 🪙 Crypto Markets
    override fun getCryptoMarkets(): Flow<List<CryptoItem>> = flow {
        try {
            val response = cryptoApi.getCryptoMarkets()
            val mapped = response.map { dto ->
                CryptoItem(
                    id = dto.id,
                    name = dto.name,
                    symbol = dto.symbol.uppercase(),
                    currentPrice = dto.current_price,
                    priceChangePercentage24h = dto.price_change_percentage_24h,
                    sparklineData = dto.sparkline_in_7d?.price?.map { it.toFloat() } ?: emptyList(),
                    iconUrl = dto.image
                )
            }
            emit(mapped)
        } catch (e: Exception) {
            emit(
                listOf(
                    CryptoItem(
                        id = "error",
                        name = "Crypto Error: ${e.javaClass.simpleName} - ${e.localizedMessage ?: "Unknown"}...",
                        symbol = "ERR",
                        currentPrice = 0.0,
                        priceChangePercentage24h = 0.0,
                        sparklineData = emptyList(),
                        iconUrl = ""
                    )
                )
            )
        }
    }

    // 🌍 Live Exchange Rates
    override fun getLiveExchangeRates(): Flow<List<CurrencyItem>> = flow {
        val finalCurrencies = mutableListOf<CurrencyItem>()

        try {
            val response = currencyApi.getLiveExchangeRates()
            val serverRates = response.conversion_rates

            val worldCurrencies = listOf(
                "USD" to ("US Dollar" to 124.50), 
                "EUR" to ("Euro (Europe)" to 134.20), 
                "GBP" to ("British Pound" to 158.40)
            )
            
            worldCurrencies.forEach { (code, data) ->
                val serverRate = serverRates[code]
                val livePrice = if (serverRate != null && serverRate > 0.0) {
                    (serverRates["ETB"] ?: 124.50) / serverRate
                } else {
                    data.second
                }
                // 🔍 ትክክለኛዎቹን ፊልዶች buyPrice እና sellPrice በመጠቀም
                finalCurrencies.add(CurrencyItem(data.first, code, livePrice, livePrice * 1.02, "https://flagcdn.com/w40/${code.take(2).lowercase()}.png"))
            }

        } catch (e: Exception) {
            val errorMsg = "${e.javaClass.simpleName}: ${e.localizedMessage ?: "Connection failed"}"
            // 🔍 እዚህም በትክክለኛው የ Model ስም ተስተካክሏል
            finalCurrencies.add(
                CurrencyItem(
                    bankName = "⚠️ Currency Error: $errorMsg",
                    bankCode = "ERR",
                    buyPrice = 0.0,
                    sellPrice = 0.0,
                    flagUrl = ""
                )
            )
        }

        emit(finalCurrencies)
    }

    // 📈 Top Stocks
    override fun getTopStocks(): Flow<List<StockItem>> = flow {
        val finalStocks = mutableListOf<StockItem>()
        
        try {
            val quote = stockApi.getStockQuote("AAPL")
            val actualPrice = if (quote.c > 0.0) quote.c else 315.41
            
            finalStocks.add(
                StockItem(
                    symbol = "AAPL",
                    name = "Apple Inc. (Live OK)",
                    price = actualPrice,
                    changePercent = quote.dp,
                    sparklineData = listOf(actualPrice.toFloat()),
                    imageUrl = ""
                )
            )
        } catch (e: Exception) {
            val errorMsg = "${e.javaClass.simpleName}: ${e.localizedMessage ?: "API Limit/Error"}"
            finalStocks.add(
                StockItem(
                    symbol = "ERR",
                    name = "⚠️ Stock Error: $errorMsg",
                    price = 0.0,
                    changePercent = 0.0,
                    sparklineData = emptyList(),
                    imageUrl = ""
                )
            )
        }

        emit(finalStocks)
    }
}