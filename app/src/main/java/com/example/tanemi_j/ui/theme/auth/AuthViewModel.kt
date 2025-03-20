package com.example.tanemi_j.ui.theme.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanemi_j.ui.theme.auth.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


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

    var originalText = mutableStateOf("")
    var translatedText = mutableStateOf("")

    private val _currentUser = MutableStateFlow(userRepository.getCurrentUser())
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    // Agregamos el parámetro "name" para registrarlo en la base de datos
    fun registerUser(email: String, password: String, name: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        userRepository.registerUser(email, password, name, onSuccess, onError)
    }

    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
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
    }

    // Función para cerrar sesión
    fun logoutUser() {
        userRepository.logoutUser() // Llamamos al repositorio para cerrar sesión en Firebase
        _userName.value = ""  // Limpiamos el nombre de usuario
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
}
