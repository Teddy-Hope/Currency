package com.currency.app.domain.usecase

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class GetFinancialAdviceUseCase @Inject constructor(
    private val geminiModel: GenerativeModel
) {
    private val systemInstruction = """
        You are an elite financial advisor assistant in the "Currency" mobile app. 
        Your expertise spans global stock markets, top cryptocurrencies, and macroeconomic landscapes.
    """.trimIndent()

    suspend fun execute(userPrompt: String): String {
        return try {
            val response = geminiModel.generateContent(
                content {
                    text(systemInstruction)
                    text(userPrompt)
                }
            )
            response.text ?: "Empty response received from AI model."
        } catch (e: Exception) {
            e.printStackTrace()
            // 🔍 ኤረሩን ሳንደብቅ በትክክል እንዲያሳየን እዚህ ጋር እናወጣዋለን
            "AI_CRASH_DEBUG -> [${e.javaClass.simpleName}]: ${e.localizedMessage ?: e.toString()}"
        }
    }
}