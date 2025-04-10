package com.example.tanemi_j

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }

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
