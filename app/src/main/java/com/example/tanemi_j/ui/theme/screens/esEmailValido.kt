package com.example.tanemi_j.ui.theme.screens

fun esEmailValido(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
