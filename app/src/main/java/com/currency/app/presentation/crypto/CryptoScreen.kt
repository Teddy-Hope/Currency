package com.currency.app.presentation.crypto

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.currency.app.domain.model.CryptoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoScreen(viewModel: CryptoViewModel) {
    val cryptoList by viewModel.cryptoList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var sortByPrice by remember { mutableStateOf(false) }

    // 🔍 ካንተ እውነተኛ የ CryptoItem አባላት (name, symbol) ጋር የተገጠመ የፍለጋ ማጣሪያ
    val filteredList = cryptoList.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.symbol.contains(searchQuery, ignoreCase = true)
    }.let { list ->
        if (sortByPrice) list.sortedByDescending { it.currentPrice } else list.sortedByDescending { it.priceChangePercentage24h }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 🔍 አዲሱ የሚያምር የክሪፕቶ መፈለጊያ ሳጥን (Search Bar)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search crypto (e.g. Bitcoin, BTC)...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // 🎛️ የዋጋ እና የትርፍ/ኪሳራ ማጣሪያ ቁልፎች (Filter Chips)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = !sortByPrice,
                onClick = { sortByPrice = false },
                label = { Text("Top Gainers (24h %)") }
            )
            FilterChip(
                selected = sortByPrice,
                onClick = { sortByPrice = true },
                label = { Text("Highest Price") }
            )
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { crypto ->
                        CryptoCard(crypto = crypto)
                    }
                }
            }
        }
    }
}

@Composable
fun CryptoCard(crypto: CryptoItem) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ ካንተ እውነተኛ የ 'iconUrl' መለኪያ ጋር ተገናኝቷል
            AsyncImage(
                model = crypto.iconUrl,
                contentDescription = crypto.name,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = crypto.name, style = MaterialTheme.typography.titleMedium)
                Text(text = crypto.symbol, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            // 📈 የ Custom Canvas Sparkline መስመር ግራፍ መሳያ (ካንተ sparklineData ጋር ተገናኝቷል)
            if (crypto.sparklineData.isNotEmpty()) {
                val isPositive = crypto.priceChangePercentage24h >= 0
                Canvas(modifier = Modifier.width(80.dp).height(35.dp)) {
                    val path = Path()
                    val maxVal = crypto.sparklineData.maxOrNull() ?: 1f
                    val minVal = crypto.sparklineData.minOrNull() ?: 0f
                    val delta = if (maxVal - minVal == 0f) 1f else maxVal - minVal
                    
                    crypto.sparklineData.forEachIndexed { index, price ->
                        val x = (index.toFloat() / (crypto.sparklineData.size - 1)) * size.width
                        val y = size.height - ((price - minVal) / delta) * size.height
                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    drawPath(
                        path = path,
                        color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                // ✅ ካንተ እውነተኛ 'currentPrice' እና 'priceChangePercentage24h' ጋር ተገናኝቷል
                Text(text = "$${String.format("%.2f", crypto.currentPrice)}", style = MaterialTheme.typography.titleMedium)
                val isPositive = crypto.priceChangePercentage24h >= 0
                Text(
                    text = "${if (isPositive) "+" else ""}${String.format("%.2f", crypto.priceChangePercentage24h)}%",
                    color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}