package com.example.tanemi_j

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.tanemi_j.ui.theme.auth.AuthViewModel
import com.example.tanemi_j.ui.theme.auth.AuthViewModelFactory
import com.example.tanemi_j.ui.theme.auth.UserRepository
import com.example.tanemi_j.ui.theme.screens.AppNavigation
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar TextToSpeech
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US
            }
        }

        val userRepository = UserRepository(FirebaseAuth.getInstance())
        val authViewModelFactory = AuthViewModelFactory(userRepository)
        val authViewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)

        // Establecer el contenido y pasar textToSpeech a AppNavigation
        setContent {
            AppNavigation(authViewModel = authViewModel, textToSpeech = textToSpeech)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Limpiar TextToSpeech cuando la actividad se destruya
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
