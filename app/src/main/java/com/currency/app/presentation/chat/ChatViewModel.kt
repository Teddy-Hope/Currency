package com.currency.app.presentation.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.app.domain.usecase.GetFinancialAdviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getFinancialAdviceUseCase: GetFinancialAdviceUseCase
) : ViewModel() {

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> get() = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // Add user message immediately
        _messages.add(ChatMessage(text = text, isUser = true))
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Execute the Gemini UseCase
                val aiResponse = getFinancialAdviceUseCase.execute(text)
                _messages.add(ChatMessage(text = aiResponse, isUser = false))
            } catch (e: Exception) {
                _messages.add(ChatMessage(text = "Error: ${e.localizedMessage ?: "Something went wrong"}", isUser = false))
            } finally {
                _isLoading.value = false
            }
        }
    }
}