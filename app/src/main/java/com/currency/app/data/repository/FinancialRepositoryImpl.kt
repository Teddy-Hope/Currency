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
            emit(emptyList())
        }
    }

    // 🌍 Live Exchange Rates (Full List Restored)
    override fun getLiveExchangeRates(): Flow<List<CurrencyItem>> = flow {
        val finalCurrencies = mutableListOf<CurrencyItem>()

        // የኢትዮጵያ ንግድ ባንኮች እና ተቋማት
        finalCurrencies.add(CurrencyItem("Commercial Bank of Ethiopia", "CBE", 123.50, 125.90, "https://flagcdn.com/w40/et.png"))
        finalCurrencies.add(CurrencyItem("Awash International Bank", "AWASH", 124.10, 126.50, "https://flagcdn.com/w40/et.png"))
        finalCurrencies.add(CurrencyItem("Dashen Bank", "DASHEN", 123.80, 126.20, "https://flagcdn.com/w40/et.png"))
        finalCurrencies.add(CurrencyItem("Abyssinia Bank", "BOA", 124.00, 126.40, "https://flagcdn.com/w40/et.png"))
        finalCurrencies.add(CurrencyItem("Hibret Bank", "HIBRET", 123.75, 126.15, "https://flagcdn.com/w40/et.png"))

        val worldCurrencies = listOf(
            "USD" to ("US Dollar" to 124.50), 
            "EUR" to ("Euro (Europe)" to 134.20), 
            "GBP" to ("British Pound" to 158.40),
            "KES" to ("Kenyan Shilling" to 0.96), 
            "ZAR" to ("South African Rand" to 6.82), 
            "EGP" to ("Egyptian Pound" to 2.58),
            "AED" to ("UAE Dirham" to 33.90), 
            "SAR" to ("Saudi Riyal" to 33.20), 
            "INR" to ("Indian Rupee" to 1.49),
            "CNY" to ("Chinese Yuan" to 17.18), 
            "JPY" to ("Japanese Yen" to 0.78), 
            "CAD" to ("Canadian Dollar" to 91.40)
        )

        var serverRates: Map<String, Double>? = null
        try {
            val response = currencyApi.getLiveExchangeRates()
            serverRates = response.conversion_rates
        } catch (e: Exception) {
            serverRates = null
        }

        worldCurrencies.forEach { (code, data) ->
            val serverRate = serverRates?.get(code)
            val livePrice = if (serverRate != null && serverRate > 0.0) {
                (serverRates["ETB"] ?: 124.50) / serverRate
            } else {
                data.second
            }
            if (finalCurrencies.none { it.bankCode == code } && finalCurrencies.size < 40) {
                finalCurrencies.add(CurrencyItem(data.first, code, livePrice, livePrice * 1.02, "https://flagcdn.com/w40/${code.take(2).lowercase()}.png"))
            }
        }

        emit(finalCurrencies)
    }

    // 📈 Top Stocks (Full 8 Elite Companies Restored)
    override fun getTopStocks(): Flow<List<StockItem>> = flow {
        val finalStocks = mutableListOf<StockItem>()
        
        val eliteCompanies = listOf(
            "AAPL" to ("Apple Inc." to 315.41),
            "MSFT" to ("Microsoft Corp." to 442.10),
            "GOOGL" to ("Alphabet Inc." to 178.50),
            "AMZN" to ("Amazon Inc." to 189.20),
            "NVDA" to ("NVIDIA Corporation" to 925.00),
            "TSLA" to ("Tesla Inc." to 175.80),
            "META" to ("Meta Platforms" to 495.30),
            "NFLX" to ("Netflix Inc." to 610.40)
        )

        eliteCompanies.forEach { (sym, data) ->
            var actualPrice = data.second
            var actualChange = 1.25
            try {
                val quote = stockApi.getStockQuote(sym)
                if (quote.c > 0.0) {
                    actualPrice = quote.c
                    actualChange = quote.dp
                }
            } catch (e: Exception) {
                actualPrice = data.second
                actualChange = 0.5
            }

            finalStocks.add(
                StockItem(
                    symbol = sym,
                    name = data.first,
                    price = actualPrice,
                    changePercent = actualChange,
                    sparklineData = listOf(actualPrice.toFloat() * 0.99f, actualPrice.toFloat() * 1.01f, actualPrice.toFloat()),
                    imageUrl = "https://www.google.com/s2/favicons?sz=128&domain=${data.first.split(" ")[0].lowercase()}.com"
                )
            )
        }

        emit(finalStocks)
    }
}