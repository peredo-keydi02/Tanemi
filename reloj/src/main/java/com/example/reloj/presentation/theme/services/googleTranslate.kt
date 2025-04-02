package com.example.reloj.presentation.theme.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URLEncoder

suspend fun googleTranslate(text: String, sourceLang: String, targetLang: String): String {
    val encodedText = URLEncoder.encode(text, "UTF-8")
    val url = "https://translate.google.com/m?hl=$sourceLang&sl=$sourceLang&tl=$targetLang&q=$encodedText"

    return withContext(Dispatchers.IO) {
        try {
            val doc = Jsoup.connect(url).get()
            val translatedText = doc.select("div.result-container").text()
            if (translatedText.isNotEmpty()) {
                return@withContext translatedText
            } else {
                return@withContext "Error: No se pudo obtener la traducción"
            }
        } catch (e: Exception) {
            return@withContext "Error en la traducción: ${e.message}"
        }
    }
}
