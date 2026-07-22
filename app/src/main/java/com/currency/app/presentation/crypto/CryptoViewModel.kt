package com.currency.app.presentation.crypto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.app.domain.model.CryptoItem
import com.currency.app.domain.repository.FinancialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val repository: FinancialRepository
) : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<CryptoItem>>(emptyList())
    val cryptoList: StateFlow<List<CryptoItem>> = _cryptoList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchCryptoData()
    }

    fun fetchCryptoData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getCryptoMarkets().collect { items ->
                _cryptoList.value = items
                _isLoading.value = false
            }
        }
    }
}