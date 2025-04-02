package com.example.reloj.presentation.theme.Auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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

 //keydi-reloj
    //lloremos juntos

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


