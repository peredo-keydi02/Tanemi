package com.example.tanemi_j

import android.content.ContentValues.TAG
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.tanemi_j.ui.theme.auth.AuthViewModel
import com.example.tanemi_j.ui.theme.auth.AuthViewModelFactory
import com.example.tanemi_j.ui.theme.auth.UserRepository
import com.example.tanemi_j.ui.theme.screens.AppNavigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
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

        // Obtener el token de FCM
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Error al obtener token FCM", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d(TAG, "Token FCM obtenido: $token")
            // Aquí podrías enviar el token a la base de datos, o confiar en onNewToken() del servicio.
            // Por ejemplo, podrías llamar a una función similar a sendTokenToServer(token)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Limpiar TextToSpeech cuando la actividad se destruya
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
