package com.example.tanemi_j.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.auth.AuthViewModel


@Composable
fun RegistroScreen(navController: NavHostController, viewModel: AuthViewModel) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") } // 🔹 Estado para mostrar mensajes de error

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC2E8FF))
            .padding(16.dp)
    ) {
        // Botón de imagen en la esquina superior izquierda
        IconButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.x),
                contentDescription = "cancelar",
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a Tanemí, Regístrate",
                color = Color.Black,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 20.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF90CAF9), shape = RoundedCornerShape(10.dp))
                    .border(3.dp, Color.White, RoundedCornerShape(10.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RegistroInputField("Nombre", nombre) { nombre = it }
                    RegistroInputField("Correo Electrónico", email, KeyboardType.Email) { email = it }
                    RegistroInputField("Contraseña", password, isPassword = true) { password = it }
                    RegistroInputField("Confirmar Contraseña", confirmPassword, isPassword = true) { confirmPassword = it }
                }
            }

            // 🔹 Mensaje de error si hay problemas con el registro
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        nombre.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                            errorMessage = "Debe llenar todos los campos."
                        !esEmailValido(email) ->
                            errorMessage = "Ingrese un correo electrónico válido."
                        password.length < 8 ->
                            errorMessage = "La contraseña debe tener al menos 8 caracteres."
                        password != confirmPassword ->
                            errorMessage = "Las contraseñas no coinciden."
                        else -> {
                            loading = true
                            viewModel.registerUser(email, password, nombre,  // Agregar el parámetro "nombre"
                                onSuccess = {
                                    loading = false
                                    navController.navigate("login")
                                },
                                onError = { msg ->
                                    loading = false
                                    errorMessage = msg
                                }
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text("Registrar", color = Color.White, fontSize = 22.sp)
            }


            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = { navController.navigate("login") },
                modifier = Modifier.padding(top = 15.dp)) {
                Text("Iniciar sesión", fontSize = 22.sp, color = Color(0xFF2196F3))
            }
        }
    }
}

@Composable
fun RegistroInputField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 22.sp, color = Color.Black, modifier = Modifier.padding(top = 10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp)) // Solo 1 fondo
                .padding(horizontal = 8.dp, vertical = 2.dp) // Padding interno
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

fun esEmailValido(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

