package com.example.tanemi_j.ui.theme.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tanemi_j.ui.theme.auth.AuthViewModel
import com.example.tanemi_j.R // Necesario para los recursos como imágenes
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.Translation
import java.util.Locale

@Composable
fun TraductorScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var translatedText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var detectedLanguage by remember { mutableStateOf("Español") }
    var targetLanguageText by remember { mutableStateOf("Inglés") }

    // Detectar el idioma del texto ingresado (esto podría hacerse con alguna librería o API)
    val sourceLanguage = if (detectedLanguage == "Español") TranslateLanguage.SPANISH else TranslateLanguage.ENGLISH
    val targetLanguage = if (detectedLanguage == "Español") TranslateLanguage.ENGLISH else TranslateLanguage.SPANISH
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(sourceLanguage)
        .setTargetLanguage(targetLanguage)
        .build()
    val translator = Translation.getClient(options)

    // Declaración de TextToSpeech
    val context = LocalContext.current // Aquí obtenemos el contexto dentro del composable
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    // Configuración de speech recognizer
    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val spokenText = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                if (spokenText != null) {
                    inputText = TextFieldValue(spokenText)
                    detectedLanguage = if (Locale.getDefault().language == "es") "Español" else "Inglés"
                    targetLanguageText = if (detectedLanguage == "Español") "Inglés" else "Español"
                }
            }
        }

    // Inicialización de TextToSpeech dentro de DisposableEffect
    DisposableEffect(context) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US // Configuración por defecto en inglés
            } else {
                Toast.makeText(context, "Error al inicializar TTS", Toast.LENGTH_SHORT).show()
            }
        }

        // Liberar los recursos de TextToSpeech cuando el composable sea destruido
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

    // La UI de la pantalla
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondosinlogo),
            contentDescription = "Fondo de la pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Escribe o usa el micrófono",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Di algo...")
                        }
                        speechRecognizerLauncher.launch(intent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.microfono),
                    contentDescription = "Micrófono",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            ElevatedButton(
                onClick = {
                    if (inputText.text.isNotBlank()) {
                        // Traducir el texto usando ML Kit
                        translator.translate(inputText.text)
                            .addOnSuccessListener { translated ->
                                translatedText = translated
                                // Usar TextToSpeech para leer la traducción
                                textToSpeech?.speak(translated, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                            .addOnFailureListener { exception ->
                                errorMessage = "Error al traducir: ${exception.message}"
                            }
                    } else {
                        errorMessage = "Ingresa una palabra para traducir"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Text("Traducir", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mostrar la traducción y el idioma
            if (translatedText.isNotEmpty()) {
                Text(
                    text = "Traducción: $translatedText\nIdioma: $targetLanguageText",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    fontSize = 18.sp,
                    color = Color.Red
                )
            }
        }
    }
}
