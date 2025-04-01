package com.example.reloj.presentation.theme.Auth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.reloj.R
import com.example.reloj.presentation.Reloj
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
        private const val CHANNEL_ID = "default_channel"
    }

    // Se invoca cuando se actualiza o genera un nuevo token FCM.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Nuevo token FCM: $token")
        // Envía el token a la base de datos para asociarlo al usuario actual.
        sendTokenToServer(token)
    }

    // Se invoca cuando se recibe un mensaje FCM.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Mensaje recibido: ${remoteMessage.data}")

        // Si el mensaje incluye un payload de notificación.
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
        } ?: run {
            // Si solo se envían datos.
            val title = remoteMessage.data["title"] ?: "Notificación"
            val body = remoteMessage.data["body"] ?: "Tienes un nuevo mensaje."
            showNotification(title, body)
        }
    }

    // Función para enviar el token a la Firebase Realtime Database.
    private fun sendTokenToServer(token: String) {
        // Aquí debes obtener el ID del usuario autenticado (por ejemplo, usando FirebaseAuth).
        // Supongamos que ya lo tienes, de lo contrario usa:
        // val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userId = "ID_DEL_USUARIO" // Reemplaza por el ID real del usuario.

        val database = FirebaseDatabase.getInstance()
        val tokensRef = database.getReference("users").child(userId).child("fcmToken")
        tokensRef.setValue(token)
            .addOnSuccessListener { Log.d(TAG, "Token guardado correctamente en la base de datos.") }
            .addOnFailureListener { e -> Log.e(TAG, "Error al guardar el token: ${e.message}") }
    }

    // Función para mostrar una notificación local.
    private fun showNotification(title: String?, message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal de notificaciones para API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Canal por defecto",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Intent que se ejecuta al pulsar la notificación (redirige a MainActivity por ejemplo)
        val intent = Intent(this, Reloj::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logocirculo) // Asegúrate de tener un drawable para la notificación.
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}