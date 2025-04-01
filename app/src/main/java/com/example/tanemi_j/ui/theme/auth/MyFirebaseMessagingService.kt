package com.example.tanemi_j.ui.theme.auth

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tanemi_j.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Este método es llamado cuando el dispositivo recibe un nuevo token de FCM
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token recibido: $token")

        // Aquí puedes enviar el token a tu base de datos
        // Llamar a tu repositorio para guardar el token
        val userRepository = UserRepository(FirebaseAuth.getInstance())
        userRepository.saveUserToken(token)
    }

    // Este método se ejecuta cuando el dispositivo recibe un mensaje
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Verificamos si el mensaje contiene datos
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Mensaje de datos recibido: " + remoteMessage.data)

            // Aquí puedes procesar los datos (por ejemplo, mostrar una notificación)
            val message = remoteMessage.data["message"] // Suponiendo que envíes un campo "message"
            showNotification(message)
        }

        // Si el mensaje contiene notificación, mostramos la notificación
        remoteMessage.notification?.let {
            Log.d("FCM", "Mensaje de notificación recibido: " + it.body)
            showNotification(it.body)
        }
    }

    // Función para mostrar una notificación
    @SuppressLint("ServiceCast")
    private fun showNotification(message: String?) {
        val channelId = "your_channel_id"
        val channelName = "Your Channel Name"
        val notificationId = 1

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Si la versión de Android es mayor a Oreo, necesitamos configurar un canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Creamos la notificación
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logocirculo)  // Cambia el icono de la notificación
            .setContentTitle("Notificación FCM")
            .setContentText(message ?: "Nuevo mensaje")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Mostramos la notificación
        //NotificationManagerCompat.from(this).notify(notificationId, notification)
    }
}