package com.example.tanemi_j.ui.theme.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            AuthViewModel(userRepository) as T
        } else {
            throw IllegalArgumentException("Modelo de clase desconocido")
        }
    }
}