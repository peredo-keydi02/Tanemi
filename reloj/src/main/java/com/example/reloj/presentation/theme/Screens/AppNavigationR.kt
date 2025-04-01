package com.example.reloj.presentation.theme.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reloj.presentation.theme.Auth.AuthViewModelR

@Composable
fun AppNavigationR(authViewModelR: AuthViewModelR) {
    val navController = rememberNavController()

 keydi-reloj
    //lloremos juntos
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreenR(navController, authViewModelR) }
        composable("menu") { MenuScreenR(navController, authViewModelR) }
        composable("traductor") { TraductorScreenR(navController, authViewModelR) }
    }
}
