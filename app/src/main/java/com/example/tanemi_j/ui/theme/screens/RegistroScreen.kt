package com.example.tanemi_j.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.Iansui
import com.example.tanemi_j.ui.theme.PoppinsNormal
import com.example.tanemi_j.ui.theme.auth.AuthViewModel

@Composable
fun RegistroScreen(navController: NavHostController, viewModel: AuthViewModel) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.ftanemi),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xBBE0F7FF),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Bienvenido a Tanemí",
                            fontSize = 24.sp,
                            color = Color.Black
                        )
                        Text(
                            "Registro",
                            fontSize = 22.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        RegistroInputField("Nombre", nombre) { nombre = it }
                        RegistroInputField("Correo Electrónico", email, KeyboardType.Email) { email = it }
                        RegistroInputField("Contraseña", password, isPassword = true) { password = it }
                        RegistroInputField("Confirmar Contraseña", confirmPassword, isPassword = true) { confirmPassword = it }

                        if (errorMessage.isNotEmpty()) {
                            Text(errorMessage, color = Color.Red, fontSize = 14.sp)
                        }

                        // Botón con diseño de gradiente
                        Box(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .border(
                                    width = 2.dp,
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF6C63FF), Color(0xFF03DAC5))
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                                .background(Color.Transparent, RoundedCornerShape(20.dp))
                                .clickable {
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
                                            viewModel.registerUser(email, password, nombre,
                                                onSuccess = {
                                                    loading = false
                                                    showDialog = true
                                                },
                                                onError = { msg ->
                                                    loading = false
                                                    errorMessage = msg
                                                }
                                            )
                                        }
                                    }
                                }
                        ) {
                            Text(
                                text = "Registrarse",
                                fontSize = 18.sp,
                                color = Color(0xFF6C63FF),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        TextButton(onClick = { navController.navigate("login") }) {
                            Text("¿Ya tienes cuenta? Inicia sesión", color = Color.Blue)
                        }
                    }
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { /* No cerrar con clic afuera */ },
                        title = {
                            Text(
                                text = "Registro exitoso",
                                fontSize = 32.sp, // Tamaño de letra grande
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(
                                    brush = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))),
                                    fontFamily = Iansui
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp)
                            )
                        },
                        text = {
                            Text(
                                text = "¡Su cuenta ha sido creada! Por favor, inicie sesión.",
                                fontSize = 22.sp, // Tamaño del texto
                                style = TextStyle(fontFamily = PoppinsNormal), // Fuente sans-serif
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Normal,// Centrar el texto
                                color = Color(0xFF1C8ADB),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                    navController.navigate("login")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Fondo azul
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .height(50.dp)
                                    .wrapContentWidth()
                                    .border(2.dp, Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))), RoundedCornerShape(50.dp))
                            ) {
                                Text(
                                    text = "Aceptar",
                                    fontSize = 22.sp, // Tamaño de letra más grande
                                    fontWeight = FontWeight.SemiBold, // Negrita
                                    style = TextStyle(
                                        brush = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))),
                                        fontFamily = Iansui
                                    ) // Texto blanco
                                )
                            }
                        },
                        containerColor = Color.White, // Fondo del diálogo azul claro
                        shape = RoundedCornerShape(16.dp) // Bordes redondeados del cuadro de diálogo
                    )
                }
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
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
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
