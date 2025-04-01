package com.example.reloj.presentation.theme.Auth

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.size > 0) {
            // Maneja los datos, puedes obtener el payload aquí
            val message = remoteMessage.data["message"]
            // Aquí podrías actualizar la UI, o hacer otras acciones según el mensaje
        }

        if (remoteMessage.notification != null) {
            // Muestra la notificación si viene una notificación
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            // Aquí muestra la notificación con NotificationManager
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Este método se llama cuando se genera un nuevo token de FCM
        // Puedes enviar este token a tu base de datos para vincularlo con un usuario
        // Por ejemplo, guardarlo en Firebase Realtime Database
    }
}
