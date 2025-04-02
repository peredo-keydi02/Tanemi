package com.example.reloj.presentation.theme.Auth


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
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


class AuthViewModelR(private val userR: UserR) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState

    private val _currentUser = MutableStateFlow(userR.getCurrentUser())
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName
<<<<<<< HEAD
 //keydi-reloj
    //lloremos juntos
=======


>>>>>>> 3fe22432e0212fa6671a8917903335553fe9ad9a
    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {

    private val _loginNotification = MutableStateFlow("")
    val loginNotification: StateFlow<String> = _loginNotification

    var originalText = mutableStateOf("")
    var translatedText = mutableStateOf("")

    /*fun loginUser(email: String, password: String, device: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            userR.getUserDevice { currentDevice ->
                if (currentDevice == null) {
                    // No hay otro dispositivo, iniciar sesión normalmente
                    userR.loginUser(email, password,
                        onSuccess = {
                            userR.setCurrentDevice(device)
                            onSuccess()
                        },
                        onError = onError
                    )
                } else if (currentDevice != device) {
                    // Ya hay un dispositivo registrado, solicitar autorización
                    userR.setPendingDevice(device)
                    _loginNotification.value = "El dispositivo $device está intentando iniciar sesión"
                } else {
                    // Mismo dispositivo, iniciar sesión normalmente
                    userR.loginUser(email, password, onSuccess, onError)
                }
            }
        }
    }*/
    fun authorizePendingDevice() {
        viewModelScope.launch {
            userR.approvePendingDevice { approvedDevice ->
                _loginNotification.value = ""
                _loginState.value = LoginResult.Success
            }
        }
    }

    fun rejectPendingDevice() {
        viewModelScope.launch {
            userR.rejectPendingDevice()
            _loginNotification.value = "Intento de inicio de sesión denegado."
        }
    }

    // Función para iniciar sesión y registrar el dispositivo
    fun loginUser(email: String, password: String, deviceState: Int, deviceModel: String, deviceState2: Int, deviceModel2: String, onSuccess: () -> Unit, onError: (String) -> Unit) {

        userR.loginUser(email, password,
            onSuccess = {
                _loginState.value = LoginResult.Success
                fetchUserName()
                userR.updateDeviceInfo(deviceState, deviceModel, deviceState2, deviceModel2) // Guardar el estado del dispositivo y modelo
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
        userR.checkDeviceState(onSuccess, onError)
    }

    fun saveTranslation(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val original = originalText.value
        val translated = translatedText.value

        if (original.isNotBlank() && translated.isNotBlank()) {
            userR.saveTranslation(original, translated, onSuccess, onError)
        } else {
            onError("Ambos campos deben estar llenos")
        }
    }

    fun fetchUserName() {
        userR.getCurrentUserName { name ->
            _userName.value = name ?: ""
        }
    }

    fun logoutUser() {
        userR.logoutUser()
    }

    fun triggerTestNotification() {
        _loginNotification.value = "Se inició sesión en otro dispositivo"
    }

    fun clearNotification() {
        _loginNotification.value = ""
    }

    fun fetchUser() {
        _currentUser.value = userR.getCurrentUser()
    }
}