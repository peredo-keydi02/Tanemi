package com.example.reloj.presentation.theme.Validations

fun esEmailValido(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
//lloremos juntos

