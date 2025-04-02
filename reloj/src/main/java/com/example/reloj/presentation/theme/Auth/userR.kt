package com.example.reloj.presentation.theme.Auth

import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class UserR(private val firebaseAuth: FirebaseAuth) {

    private val db = FirebaseDatabase.getInstance().reference // Instancia de la base de datos

    //  Función para verificar la contraseña encriptada manualmente
    fun verificarContraseña(contraseñaIngresada: String, contraseñaAlmacenada: String): Boolean {
        val resultado = BCrypt.verifyer().verify(contraseñaIngresada.toCharArray(), contraseñaAlmacenada)
        return resultado.verified
    }

    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error al iniciar sesión")
                }
            }
    }

    fun getUserDevice(onResult: (String?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("users/$userId/currentDevice")

        ref.get().addOnSuccessListener { snapshot ->
            onResult(snapshot.value as? String)
        }
    }

    fun setCurrentDevice(device: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance().getReference("users/$userId/currentDevice").setValue(device)
    }

    fun setPendingDevice(device: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance().getReference("users/$userId/pendingDevice").setValue(device)
    }

    fun approvePendingDevice(onResult: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("users/$userId")

        ref.child("pendingDevice").get().addOnSuccessListener { snapshot ->
            val approvedDevice = snapshot.value as? String
            if (approvedDevice != null) {
                ref.child("currentDevice").setValue(approvedDevice)
                ref.child("pendingDevice").removeValue()
                onResult(approvedDevice)
            }
        }
    }

    fun rejectPendingDevice() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance().getReference("users/$userId/pendingDevice").removeValue()
    }




    fun getCurrentUserName(onResult: (String?) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            db.child("users").child(uid).get().addOnSuccessListener { snapshot ->
                val name = snapshot.child("name").value as? String
                onResult(name)
            }.addOnFailureListener {
                onResult(null) // En caso de error
            }
        } else {
            onResult(null) // Usuario no autenticado
        }
    }

    fun updateDeviceInfo(deviceState: Int, deviceModel: String, deviceState2: Int, deviceModel2: String) {
        val uid = firebaseAuth.currentUser?.uid ?: return
        db.child("users").child(uid).child("deviceState").setValue(deviceState)
        db.child("users").child(uid).child("deviceModel").setValue(deviceModel)
        db.child("users").child(uid).child("deviceState2").setValue(deviceState2)
        db.child("users").child(uid).child("deviceModel2").setValue(deviceModel2)
    }

    // Función para verificar el estado del dispositivo
    fun checkDeviceState(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid ?: return
        db.child("users").child(uid).child("deviceState").get().addOnSuccessListener { snapshot ->
            val deviceState = snapshot.getValue(Int::class.java) ?: 0
            val deviceModel = snapshot.child("deviceModel").getValue(String::class.java) ?: ""

            if (deviceState == 1) {
                onSuccess("Se ha vinculado al dispositivo $deviceModel")
            } else {
                onError("No ha iniciado sesión en ningún otro dispositivo")
            }
        }.addOnFailureListener {
            onError("Error al obtener la información del dispositivo")
        }
    }





    // Función para cerrar sesión
    fun logoutUser() {
        firebaseAuth.signOut()  // Cierra sesión en Firebase
    }

    fun getCurrentUser() = firebaseAuth.currentUser

    fun saveTranslation(originalText: String, translatedText: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            val translationRef = db.child("users").child(uid).child("history").push()
            val translationData = mapOf(
                "original" to originalText,
                "translated" to translatedText,
                "timestamp" to System.currentTimeMillis()
            )

            translationRef.setValue(translationData)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { error -> onError(error.message ?: "Error al guardar traducción") }
        } else {
            onError("No se ha logeado")
        }
    }

}


