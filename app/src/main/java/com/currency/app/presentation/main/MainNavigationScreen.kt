package com.currency.app.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.currency.app.presentation.chat.ChatScreen
import com.currency.app.presentation.chat.ChatViewModel
import com.currency.app.presentation.crypto.CryptoScreen
import com.currency.app.presentation.crypto.CryptoViewModel
import com.currency.app.presentation.stock.StockScreen
import com.currency.app.presentation.stock.StockViewModel
import com.currency.app.presentation.currency.CurrencyScreen
import com.currency.app.presentation.currency.CurrencyViewModel

enum class Screen(val title: String) {
    Currency("Currency"),
    Crypto("Crypto"),
    Stock("Stocks"),
    Chat("AI Assistant")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationScreen() {
    var currentScreen by remember { mutableStateOf(Screen.Currency) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScreen.title + " - Currency & Markets AI") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == Screen.Currency,
                    onClick = { currentScreen = Screen.Currency },
                    label = { Text("Currency") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Currency") }
                )
                NavigationBarItem(
                    selected = currentScreen == Screen.Crypto,
                    onClick = { currentScreen = Screen.Crypto },
                    label = { Text("Crypto") },
                    icon = { Icon(Icons.Default.Build, contentDescription = "Crypto") }
                )
                NavigationBarItem(
                    selected = currentScreen == Screen.Stock,
                    onClick = { currentScreen = Screen.Stock },
                    label = { Text("Stocks") },
                    icon = { Icon(Icons.Default.Email, contentDescription = "Stocks") }
                )
                NavigationBarItem(
                    selected = currentScreen == Screen.Chat,
                    onClick = { currentScreen = Screen.Chat },
                    label = { Text("AI Assistant") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "AI Chat") }
                )
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.Currency -> {
                    val currencyViewModel: CurrencyViewModel = hiltViewModel()
                    CurrencyScreen(viewModel = currencyViewModel)
                }
                Screen.Crypto -> {
                    val cryptoViewModel: CryptoViewModel = hiltViewModel()
                    CryptoScreen(viewModel = cryptoViewModel)
                }
                Screen.Stock -> {
                    val stockViewModel: StockViewModel = hiltViewModel()
                    StockScreen(viewModel = stockViewModel)
                }
                Screen.Chat -> {
                    val chatViewModel: ChatViewModel = hiltViewModel()
                    ChatScreen(viewModel = chatViewModel, onNavigateBack = { currentScreen = Screen.Currency })
                }
            }
        }
    }
}