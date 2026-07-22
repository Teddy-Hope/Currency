package com.currency.app.domain.usecase

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

/**
 * UseCase handling business logic for interacting with the Gemini AI Financial Assistant.
 */
class GetFinancialAdviceUseCase @Inject constructor(
    private val geminiModel: GenerativeModel
) {
    // System instructions to tightly bound the AI into a professional financial domain
    private val systemInstruction = """
        You are an elite financial advisor assistant in the "Currency" mobile app. 
        Your expertise spans global stock markets (Tesla, Amazon, Google), top cryptocurrencies (BTC, ETH), 
        and specifically the macroeconomic landscape of Ethiopia (foreign exchange rates, banking policies, and financial reforms).
        Provide highly accurate, data-driven, practical, and concise financial explanations. 
        Do not answer queries completely unrelated to finance, economy, or investment.
    """.trimIndent()

    /**
     * Sends a secure prompt to Gemini and streams the structured textual response.
     */
    suspend fun execute(userPrompt: String): String {
        return try {
            val response = geminiModel.generateContent(
                content {
                    text(systemInstruction)
                    text(userPrompt)
                }
            )
            response.text ?: "I am unable to analyze this data at the moment. Please try again."
        } catch (e: Exception) {
            e.printStackTrace()
            "Network error: Unable to connect to the financial AI engine."
        }
    }
}