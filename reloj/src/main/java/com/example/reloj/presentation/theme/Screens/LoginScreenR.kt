package com.example.reloj.presentation.theme.Screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.*
import com.example.reloj.R
import com.example.reloj.presentation.theme.Auth.AuthViewModelR
import com.example.reloj.presentation.theme.Iansui
import com.example.reloj.presentation.theme.Validations.esEmailValido
import kotlinx.coroutines.delay
import androidx.wear.compose.material.Button
import com.example.reloj.presentation.theme.PoppinsNormal

@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreenR(navController: NavController, authViewModelR: AuthViewModelR) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isClosing by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity
    val listState = rememberScalingLazyListState()
    var showDialog by remember { mutableStateOf(false) }
    var showDialogC by remember { mutableStateOf(false) }


    if (isClosing) {
        LaunchedEffect(Unit) {
            delay(300)
            activity?.finish()
        }
    }

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = R.drawable.fondob), // Reemplaza con tu imagen
                contentDescription = "Fondo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            AnimatedVisibility(
                visible = !isClosing,
                exit = scaleOut(animationSpec = tween(300), targetScale = 0.7f) + fadeOut(animationSpec = tween(300))
            ) {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = listState // Solución al error
                ) {
                    item {
                        Image(
                            painter = painterResource(id = R.drawable.logocirculo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(80.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    item {
                        Text(
                            text = "Inicio de sesión",
                            fontSize = 18.sp,
                            color = Color(0xFF1C8ADB), // Cambia el color para que contraste con el fondo
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontFamily = Iansui)
                        )
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0x99C2E8FF), // Color con transparencia
                                    shape = RoundedCornerShape(10.dp) // Bordes redondeados
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                LoginInputFieldWear("Correo", email) { email = it }
                                Spacer(modifier = Modifier.height(8.dp)) // Espacio entre inputs
                                LoginInputFieldWear("Contraseña", password, isPassword = true) { password = it }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    if (errorMessage.isNotEmpty()) {
                        item {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .height(30.dp)
                                .wrapContentSize()
                                .border(
                                    width = 2.dp,
                                    brush = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))), // Degradado
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .clickable {
                                    when {
                                        email.isBlank() -> errorMessage = "Ingrese su correo"
                                        !esEmailValido(email) -> errorMessage = "Correo incorrecto"
                                        password.isBlank() -> errorMessage = "Ingrese su contraseña"
                                        else -> {
                                            authViewModelR.loginUser(
                                                email,
                                                password,
                                                deviceState2 = 1,
                                                deviceModel2 = "Reloj Tanemí",
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
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Iniciar sesión",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
                                style = TextStyle(
                                    fontFamily = Iansui,
                                    brush = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF)))
                                )
                            )
                        }
                    }

                    item {
                        TextButton(onClick = { showDialogC = true},
                            modifier = Modifier.fillMaxWidth()) {
                            Text("¿Olvidaste tu contraseña?", fontSize = 12.sp, color = Color(0xFF1C8ADB), style = TextStyle(fontFamily = PoppinsNormal), fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center)
                        }

                        if (showDialogC) {
                            AlertDialog(
                                onDismissRequest = { showDialogC = false },
                                title = {
                                    Text(
                                        "Aviso",
                                        fontSize = 18.sp,
                                        style = TextStyle(
                                            brush = Brush.horizontalGradient(
                                                listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))
                                            ),
                                            fontFamily = Iansui
                                        ),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        "Instale Tanemí para cambiar la contraseña",
                                        fontSize = 10.sp,
                                        style = TextStyle(fontFamily = PoppinsNormal),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF1C8ADB),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                confirmButton = {
                                    Button(
                                        onClick = { showDialogC = false },
                                        colors = androidx.wear.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                        shape = RoundedCornerShape(25.dp)

                                    ) {
                                        Text(
                                            "X",
                                            style = TextStyle(
                                                brush = Brush.horizontalGradient(
                                                    listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))
                                                ),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(4.dp) // Agrega padding manualmente
                                        )
                                    }
                                }

                            )
                        }
                    }

                    item {
                        TextButton(onClick = { showDialog = true }) {
                            Text("Registro", fontSize = 14.sp, color = Color(0xFF1C8ADB), style = TextStyle(fontFamily = PoppinsNormal), fontWeight = FontWeight.Normal)
                        }

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = {
                                    Text(
                                        "Aviso",
                                        fontSize = 18.sp,
                                        style = TextStyle(
                                            brush = Brush.horizontalGradient(
                                                listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))
                                            ),
                                            fontFamily = Iansui
                                        ),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        "Instale Tanemí en su teléfono para ser parte de nuestra familia",
                                        fontSize = 10.sp,
                                        style = TextStyle(fontFamily = PoppinsNormal),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF1C8ADB),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                confirmButton = {
                                    Button(
                                        onClick = { showDialog = false },
                                        colors = androidx.wear.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                        shape = RoundedCornerShape(25.dp)

                                    ) {
                                        Text(
                                            "X",
                                            style = TextStyle(
                                                brush = Brush.horizontalGradient(
                                                    listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))
                                                ),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(4.dp) // Agrega padding manualmente
                                        )
                                    }
                                }

                            )
                        }
                    }

                    item {
                        IconButton(onClick = { isClosing = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.x),
                                contentDescription = "Cerrar",
                                tint = Color(0xFF1C8ADB),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginInputFieldWear(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF155DC0),
            modifier = Modifier.padding(bottom = 2.dp),
            style = TextStyle(fontFamily = PoppinsNormal)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x99FFFFFF), shape = RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                singleLine = true

            )
        }
    }
}
