package com.example.tanemi_j.ui.theme.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


sealed class LoginResult {
    object Idle : LoginResult()
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState

    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName

    private val _loginNotification = MutableStateFlow("")
    val loginNotification: StateFlow<String> = _loginNotification

    var originalText = mutableStateOf("")
    var translatedText = mutableStateOf("")

    private val _currentUser = MutableStateFlow(userRepository.getCurrentUser())
    val currentUser: StateFlow<FirebaseUser?> = _currentUser



    /*init {
        listenForLoginChanges()
    }

    fun loginUser(email: String, password: String, device: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        userRepository.loginUser(email, password,
            onSuccess = {
                _loginState.value = LoginResult.Success
                fetchUserName()
                updateLoginStatus(device)
                onSuccess()
            },
            onError = { error ->
                _loginState.value = LoginResult.Error(error)
                onError(error)
            }
        )
    }
    private fun updateLoginStatus(device: String) {
        userRepository.updateLoginStatus(device)
    }

    private fun listenForLoginChanges() {
        userRepository.listenForLoginChanges { device ->
            _loginNotification.value = "Se inició sesión en $device"
        }
    }*/
    // Agregamos el parámetro "name" para registrarlo en la base de datos
    fun registerUser(email: String, password: String, name: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        userRepository.registerUser(email, password, name, onSuccess, onError)
    }

    // Función para iniciar sesión y registrar el dispositivo
    fun loginUser(email: String, password: String, deviceState: Int, deviceModel: String, deviceState2: Int, deviceModel2: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        userRepository.loginUser(email, password,
            onSuccess = {
                _loginState.value = LoginResult.Success
                fetchUserName() // Cargar el nombre del usuario
                userRepository.updateDeviceInfo(deviceState, deviceModel, deviceState2, deviceModel2) // Guardar el estado del dispositivo y modelo
                onSuccess()
            },
            onError = { error ->
                _loginState.value = LoginResult.Error(error)
                onError(error)
            }
        )
    }

    // Función para verificar el estado del dispositivo y mostrar mensaje
    fun checkDeviceState(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        userRepository.checkDeviceState(onSuccess, onError)
    }





    /*fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        userRepository.loginUser(email, password,
            onSuccess = {
                _loginState.value = LoginResult.Success
                fetchUserName() // Cargar el nombre del usuario al iniciar sesión
                onSuccess()
            },
            onError = { error ->
                _loginState.value = LoginResult.Error(error)
                onError(error)
            }
        )
    }*/
    // Función para cerrar sesión
    fun logoutUser() {
        userRepository.logoutUser() // Llamamos al repositorio para cerrar sesión en Firebase
        _userName.value = ""  // Limpiamos el nombre de usuario
    }
    ///funcion para mandar correo
    fun enviarCorreoRecuperacion(email: String, callback: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }


    fun fetchUserName() {
        userRepository.getCurrentUserName { name ->
            _userName.value = name ?: ""
        }
    }

    fun fetchUser() {
        _currentUser.value = userRepository.getCurrentUser()
    }

    fun getCurrentUser() = userRepository.getCurrentUser()

    fun saveTranslation(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val original = originalText.value
        val translated = translatedText.value

        if (original.isNotBlank() && translated.isNotBlank()) {
            userRepository.saveTranslation(original, translated, onSuccess, onError)
        } else {
            onError("Ambos campos deben estar llenos")
        }
    }

    fun triggerTestNotification() {
        _loginNotification.value = "Se inició sesión en otro dispositivo"
    }

    fun clearNotification() {
        _loginNotification.value = ""
    }


}