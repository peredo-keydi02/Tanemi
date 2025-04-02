package com.example.reloj.presentation.theme.Auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModelFactory(private val userR: UserR) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AuthViewModelR::class.java)) {
            AuthViewModelR(userR) as T
        } else {
            throw IllegalArgumentException("Modelo de clase desconocido")
        }
    }

