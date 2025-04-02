package com.example.reloj.presentation.theme.Auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
 //keydi-reloj
    //lloremos juntos
    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        userR.loginUser(email, password,
            onSuccess = {
                _loginState.value = LoginResult.Success
                onSuccess()
            },
            onError = { error ->
                _loginState.value = LoginResult.Error(error)
                onError(error)
            }
        )
    }

    fun fetchUserName() {
        userR.getCurrentUserName { name ->
            _userName.value = name ?: ""
        }
    }

    fun logoutUser() {
        userR.logoutUser()
    }

    fun fetchUser() {
        _currentUser.value = userR.getCurrentUser()
    }
}