package com.example.tanemi_j.ui.theme.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tanemi_j.ui.theme.auth.AuthViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("registro") { RegistroScreen(navController, authViewModel) }
        composable("menu") { MenuScreen(navController, authViewModel) }
        composable("traductor") { TraductorScreen(navController, authViewModel) }
        composable("recuperarcontrasena") { RecuperarContrasena(navController, authViewModel) }
        composable("modificarcontrasena") { ModificarContrasenaScreen(navController,authViewModel) }
    }
}

