package com.example.tanemi_j.ui.theme.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class UserRepository(private val firebaseAuth: FirebaseAuth) {

    private val db = FirebaseDatabase.getInstance().reference // Instancia de la base de datos

    fun registerUser(email: String, password: String, name: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.currentUser?.uid

                    //  Encriptar contraseña antes de guardarla en la base de datos
                    val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())

                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "contraseña" to hashedPassword // Guardamos la contraseña encriptada
                    )

                    uid?.let {
                        db.child("users").child(it).setValue(user)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { exception ->
                                onError(exception.message ?: "Error al guardar en la base de datos")
                            }
                    } ?: onError("Error: UID nulo")
                } else {
                    onError(task.exception?.message ?: "Error en el registro")
                }
            }
    }

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



