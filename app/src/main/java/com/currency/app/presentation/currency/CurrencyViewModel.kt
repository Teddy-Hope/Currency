package com.currency.app.presentation.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.app.domain.model.CurrencyItem
import com.currency.app.domain.repository.FinancialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: FinancialRepository
) : ViewModel() {

    private val _currencyList = MutableStateFlow<List<CurrencyItem>>(emptyList())
    val currencyList: StateFlow<List<CurrencyItem>> = _currencyList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchCurrencyData()
    }

    fun fetchCurrencyData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getLiveExchangeRates().collect { items ->
                _currencyList.value = items
                _isLoading.value = false
            }
        }
    }
}