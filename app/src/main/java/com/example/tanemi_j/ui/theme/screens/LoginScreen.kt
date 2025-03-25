package com.example.tanemi_j.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.tanemi_j.ui.theme.auth.AuthViewModel
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.Iansui
import com.example.tanemi_j.ui.theme.PoppinsNormal
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var termsChecked by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isClosing by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    if (isClosing) {
        LaunchedEffect(Unit) {
            delay(300) // Espera la animación antes de cerrar
            activity?.finish()
        }
    }

    AnimatedVisibility(
        visible = !isClosing,
        exit = scaleOut(animationSpec = tween(300), targetScale = 0.7f) + fadeOut(animationSpec = tween(300)) // 🔥 Nueva animación: Zoom Out + Fade
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.taemif),
                contentDescription = "Fondo",
                modifier = Modifier.size(909.dp).align(Alignment.TopCenter),
                contentScale = ContentScale.Fit
            )

            // Botón de cierre con nueva animación
            IconButton(
                onClick = { isClosing = true },
                modifier = Modifier
                    .padding(start = 16.dp, top = 30.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.x),
                    contentDescription = "Cerrar aplicación",
                    tint = Color(0xFF1C8ADB),
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Inicio de sesión",
                    fontSize = 32.sp,
                    color = Color(0xFF1C8ADB),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontFamily = Iansui),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.logocirculo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                        LoginInputField("Correo Electrónico", email) { email = it }
                        LoginInputField("Contraseña", password, isPassword = true) { password = it }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = termsChecked,
                                onCheckedChange = { termsChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF1C4975),
                                    uncheckedColor = Color(0xFF1C4975),
                                    checkmarkColor = Color.White
                                )
                            )
                            Text(
                                text = "Términos y condiciones",
                                fontSize = 16.sp,
                                color = Color(0xFF1C4975),
                                style = TextStyle(fontFamily = PoppinsNormal),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        when {
                            email.isBlank() ->
                                errorMessage = "Introduzca su correo eletroónico, por favor."
                            !esEmailValido(email) ->
                                errorMessage = "El correo ingresado es incorrecto, intente de nuevo, por favor."
                            password.isBlank() ->
                                errorMessage = "Introduzca su contraseña, por favor."
                            !termsChecked ->
                                errorMessage = "Debe aceptar los términos y condiciones."
                            else -> {
                                authViewModel.loginUser(
                                    email,
                                    password,
                                    onSuccess = {
                                        navController.navigate("menu") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    },
                                    onError = { error -> errorMessage = error }
                                )
                            }
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
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 25.sp,
                        style = TextStyle(
                            brush = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF)))
                        ),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(onClick = { navController.navigate("recuperarcontrasena") }) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontSize = 20.sp,
                        color = Color(0xFF1C8ADB),
                        style = TextStyle(fontFamily = PoppinsNormal)
                    )
                }

                TextButton(onClick = { navController.navigate("registro") }) {
                    Text(
                        text = "Registro",
                        fontSize = 23.sp,
                        color = Color(0xFF1C8ADB),
                        style = TextStyle(fontFamily = PoppinsNormal)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginInputField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(fontFamily = PoppinsNormal),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color(0xFF155DC0),
            modifier = Modifier.padding(top = 10.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp)
                .background(Color(0x99FFFFFF), shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
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
