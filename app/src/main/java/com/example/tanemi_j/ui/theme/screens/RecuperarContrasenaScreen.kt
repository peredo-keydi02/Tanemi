package com.example.tanemi_j.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.Iansui
import com.example.tanemi_j.ui.theme.PoppinsNormal
import com.example.tanemi_j.ui.theme.auth.AuthViewModel

@Composable
fun RecuperarContrasena(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        IconButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Image(
                painter = painterResource(id = R.drawable.x),
                contentDescription = "Cancelar",
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logocirculo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Text(text = "¿No recuerdas tu contraseña?",
                fontSize = 32.sp,
                color = Color(0xFF1C8ADB),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontFamily = Iansui),
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(
                        Color(0x99C2E8FF),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    RecuperarInputField("Ingresa tu correo válido", email) { email = it }
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp)) // Separa más el botón

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        loading = true
                        authViewModel.enviarCorreoRecuperacion(email) { success ->
                            if (success) {
                                loading = false
                                showDialog = true
                            } else {
                                errorMessage = "No se pudo enviar el correo. Verifica tu dirección."
                            }
                        }
                    } else {
                        errorMessage = "Por favor, ingresa tu correo electrónico."
                    }
                },
                modifier = Modifier
                    .padding(top = 15.dp)
                    .height(50.dp)
                    .wrapContentWidth()
                    .border(
                        2.dp,
                        Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))),
                        RoundedCornerShape(50.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Text(text = "Recuperar", fontSize = 25.sp, fontWeight = FontWeight.SemiBold,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))),
                        fontFamily = Iansui

                    ))

            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { /* No cerrar con clic afuera */ },
                    title = {
                        Text(
                            text = "Correo enviado",
                            fontSize = 32.sp, // Tamaño de letra grande
                            fontWeight = FontWeight.SemiBold,
                            style = TextStyle(
                                brush = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))),
                                fontFamily = Iansui
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp)// Color del texto
                        )
                    },
                    text = {
                        Text(
                            text = "¡Se ha enviado un mensaje a su correo!",
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
                                navController.navigate("login") // Redirige al login
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

@Composable
fun RecuperarInputField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = TextStyle(fontFamily = PoppinsNormal), fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = Color(0xFF1B72B3), modifier = Modifier.padding(top = 10.dp))


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
                textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }
    }
}

