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
import kotlin.random.Random

class FinancialRepositoryImpl @Inject constructor(
    private val cryptoApi: CryptoApiService,
    private val stockApi: StockApiService,
    private val currencyApi: CurrencyApiService
) : FinancialRepository {

    // 🪙 Crypto Markets (ሳይነካ እንዳለ ይቀጥላል)
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

    // 🌍 Live Exchange Rates
    override fun getLiveExchangeRates(): Flow<List<CurrencyItem>> = flow {
        val finalCurrencies = mutableListOf<CurrencyItem>()
        
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

        val extraCodes = listOf("AUD", "CHF", "RUB", "TRY", "BRL", "QAR", "KWD", "OMR", "BHD", "JOD", "SGD", "NZD", "HKD", "MYR", "THB")
        extraCodes.forEach { code ->
            if (finalCurrencies.size < 40) {
                val defaultRate = 45.0
                finalCurrencies.add(CurrencyItem("$code Forex Rate", code, defaultRate, defaultRate * 1.02, "https://flagcdn.com/w40/${code.take(2).lowercase()}.png"))
            }
        }
        emit(finalCurrencies)
    }

    // 📈 Top Stocks (የኤፒአይ ግጭትን እና የራንደም ውሸት ዋጋን በአስተማማኝ ሁኔታ ማስተካከያ)
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
            "NFLX" to ("Netflix Inc." to 610.40),
            "AMD" to ("Advanced Micro Devices" to 160.25),
            "V" to ("Visa Inc." to 275.10),
            "JPM" to ("JPMorgan Chase" to 195.40),
            "DIS" to ("The Walt Disney Co." to 112.30),
            "WMT" to ("Walmart Inc." to 60.15),
            "NKE" to ("Nike Inc." to 98.40)
        )

        eliteCompanies.forEach { (sym, data) ->
            var actualPrice = data.second
            var actualChange = 1.25
            try {
                // እያንዳንዱን ጥያቄ ለብቻ በ try-catch በመጠበቅ ሰርቨሩ ስሮጥ ትክክለኛውን እንዲወስድ ማድረግ
                val quote = stockApi.getStockQuote(sym)
                if (quote.c > 0.0) {
                    actualPrice = quote.c
                    actualChange = quote.dp
                }
            } catch (e: Exception) {
                // ኔትወርክ ወይም ሬት ሲገድብ የባዝ ዋጋውን ይጠቀማል
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

        val extraStocks = listOf(
            "INTC" to "Intel Corp.", "PYPL" to "PayPal Holdings", "ORCL" to "Oracle Corp.",
            "CSCO" to "Cisco Systems", "CRM" to "Salesforce Inc.", "SBUX" to "Starbucks Corp.", "XOM" to "Exxon Mobil"
        )
        extraStocks.forEach { (sym, name) ->
            if (finalStocks.size < 21) {
                val defaultPrice = 120.0
                finalStocks.add(
                    StockItem(
                        symbol = sym,
                        name = name,
                        price = defaultPrice,
                        changePercent = 0.85,
                        sparklineData = listOf(118.0f, 121.0f, 120.0f),
                        imageUrl = "https://www.google.com/s2/favicons?sz=128&domain=${name.split(" ")[0].lowercase()}.com"
                    )
                )
            }
        }
        emit(finalStocks)
    }
}