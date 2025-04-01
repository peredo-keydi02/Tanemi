package com.example.reloj.presentation.theme.Auth

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


open class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Si el mensaje contiene una notificación
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
            // Aquí podrías mostrar una notificación en la interfaz de usuario del dispositivo
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token: $token")

        // Aquí guardas el token en tu base de datos para luego enviar las notificaciones.
        saveTokenToDatabase(token)
    }

    private fun saveTokenToDatabase(token: String) {
        // Guardar el token en Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().reference
        val userId = "user123"  // Obtén el ID del usuario, puede ser el que usas para autenticar al usuario.
        database.child("users").child(userId).child("fcmToken").setValue(token)
    }
}
