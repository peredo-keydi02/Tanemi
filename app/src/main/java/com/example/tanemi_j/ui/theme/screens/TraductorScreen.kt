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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.Iansui
import com.example.tanemi_j.ui.theme.PoppinsBold
import com.example.tanemi_j.ui.theme.auth.AuthViewModel
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.Translation
import java.util.*

@Composable
fun TraductorScreen(navController: NavHostController, authViewModel: AuthViewModel, textToSpeech: TextToSpeech) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var translatedText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var detectedLanguage by remember { mutableStateOf("Español") }
    var targetLanguageText by remember { mutableStateOf("Inglés") }

    // Estado para alternar entre español e inglés
    val isSpanish = remember { mutableStateOf(true) }

    // Configuración de ML Kit para traducir
    val sourceLanguage = if (isSpanish.value) TranslateLanguage.SPANISH else TranslateLanguage.ENGLISH
    val targetLanguage = if (isSpanish.value) TranslateLanguage.ENGLISH else TranslateLanguage.SPANISH

    val options = TranslatorOptions.Builder()
        .setSourceLanguage(sourceLanguage)
        .setTargetLanguage(targetLanguage)
        .build()

    val translator: Translator = Translation.getClient(options)

    // Descargar el modelo de traducción
    translator.downloadModelIfNeeded()
        .addOnSuccessListener {
            // El modelo se descargó correctamente
        }
        .addOnFailureListener { exception ->
            errorMessage = "Error al descargar el modelo: ${exception.message}"
        }

    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val spokenText = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                if (spokenText != null) {
                    inputText = TextFieldValue(spokenText)
                    detectedLanguage = if (isSpanish.value) "Español" else "Inglés"
                    targetLanguageText = if (isSpanish.value) "Inglés" else "Español"
                }
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondosinlogo),
            contentDescription = "Fondo de la pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Botón de retroceso en la esquina superior izquierda
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = 26.dp, top = 40.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.regresar),
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Presione el microfono para comenzar",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(fontFamily = Iansui),
                color = Color.White,
                fontWeight = FontWeight.Normal,
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
                        translator.translate(inputText.text)
                            .addOnSuccessListener { translated ->
                                translatedText = translated
                                val language = if (isSpanish.value) Locale.US else Locale("es", "ES")
                                textToSpeech.language = language
                                textToSpeech.speak(translated, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                            .addOnFailureListener { exception ->
                                errorMessage = "Error al traducir: ${exception.message}"
                            }
                    } else {
                        errorMessage = "Ingresa una palabra para traducir"
                    }
                },
                //colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2)),
                //shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF8C52FF),
                                Color(0xFF5CE1E6)
                            )
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ),

                ) {
                Text("Traducir",fontSize = 23.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(fontFamily = PoppinsBold),
                    color = Color.White)
            }


            Spacer(modifier = Modifier.height(20.dp))

            if (translatedText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF8C52FF),
                                    Color(0xFF5CE1E6)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Traducción: $translatedText",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    fontSize = 18.sp,
                    color = Color.Red
                )
            }

            Text(
                text = "Idioma de entrada: $detectedLanguage",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "Traducido a: $targetLanguageText",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 5.dp)
            )

            ElevatedButton(
                onClick = {
                    isSpanish.value = !isSpanish.value
                    detectedLanguage = if (isSpanish.value) "Español" else "Inglés"
                    targetLanguageText = if (isSpanish.value) "Inglés" else "Español"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Text("Cambiar idioma", fontSize = 18.sp)
            }
        }
    }
}
