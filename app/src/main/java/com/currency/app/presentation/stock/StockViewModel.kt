package com.currency.app.presentation.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.app.domain.model.StockItem
import com.currency.app.domain.repository.FinancialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val repository: FinancialRepository
) : ViewModel() {

    private val _stockList = MutableStateFlow<List<StockItem>>(emptyList())
    val stockList: StateFlow<List<StockItem>> = _stockList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        startLivePriceUpdates() // 🚀 አዲስ የሲኒየር Feature: የቀጥታ የሰከንድ ዋጋ ማሻሻያ መቆጣጠሪያ
    }

    // 🔄 በየ 5 ሰከንዱ ከሰርቨር ላይ እውነተኛውን ዋጋ እየሳበ ስክሪኑን በቅጽበት የመለወጫ ሉፕ
    private fun startLivePriceUpdates() {
        viewModelScope.launch {
            _isLoading.value = _stockList.value.isEmpty()
            while (true) {
                try {
                    repository.getTopStocks().collect { items ->
                        if (items.isNotEmpty()) {
                            _stockList.value = items
                        }
                    }
                } catch (e: Exception) {
                    // የሰርቨር መቆራረጥ ካለ እንዳይቋረጥ መከላከያ
                }
                _isLoading.value = false
                delay(5000) // ⏳ በየ 5 ሰከንዱ እውነተኛውን ዋጋ ከዓለም ገበያ ይስባል
            }
        }
    }
}