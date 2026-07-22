package com.currency.app.presentation.currency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.currency.app.domain.model.CurrencyItem

@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel) {
    val currencyList by viewModel.currencyList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var usdAmount by remember { mutableStateOf("1") }

    // ✅ ካንተ እውነተኛ የ CurrencyItem አባላት (bankCode, bankName) ጋር ተገጥሟል
    val filteredList = currencyList.filter {
        it.bankCode.contains(searchQuery, ignoreCase = true) || it.bankName.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Live Currency Calculator (USD Base)", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = usdAmount,
                    onValueChange = { usdAmount = it },
                    label = { Text("Enter USD Amount ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                )
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Search currencies (e.g. CBE, USD, KES)...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { currency ->
                        val factor = usdAmount.toDoubleOrNull() ?: 1.0
                        val calculatedBuy = if (currency.bankCode == "ETB") currency.buyPrice else currency.buyPrice * factor
                        val calculatedSell = if (currency.bankCode == "ETB") currency.sellPrice else currency.sellPrice * factor
                        CurrencyCard(currency = currency, calculatedBuy = calculatedBuy, calculatedSell = calculatedSell)
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyCard(currency: CurrencyItem, calculatedBuy: Double, calculatedSell: Double) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = currency.flagUrl,
                contentDescription = currency.bankName,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = currency.bankCode, style = MaterialTheme.typography.titleMedium)
                Text(text = currency.bankName, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.End) {
                // ✅ ካንተ እውነተኛ ስሞች (buyPrice, sellPrice) ጋር ፍጹም ተገናኝቷል
                Text(text = "Buy: ${String.format("%.2f", calculatedBuy)} ETB", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                Text(text = "Sell: ${String.format("%.2f", calculatedSell)} ETB", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}