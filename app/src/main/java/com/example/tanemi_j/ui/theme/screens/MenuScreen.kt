package com.example.tanemi_j.ui.theme.screens

import android.graphics.Paint.Align
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.Iansui
import com.example.tanemi_j.ui.theme.PoppinsBold
import com.example.tanemi_j.ui.theme.PoppinsNormal
import com.example.tanemi_j.ui.theme.auth.AuthViewModel
import com.google.firebase.messaging.FirebaseMessaging


@Composable
fun MenuScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val userName by authViewModel.userName.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.fetchUserName() // Obtener el nombre al entrar en la pantalla
    }
    // Obtener el contexto de Compose
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondosinlogo), // Reemplaza con tu imagen
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Botón de imagen en la esquina superior izquierda
        IconButton(
            onClick = {
                authViewModel.logoutUser()
                navController.navigate("login") {
                    popUpTo("menu") { inclusive = true }
                }

            },
            modifier = Modifier
                .padding(start = 26.dp, top = 40.dp)
                .align(Alignment.TopStart)
        ) {
            Image(
                painter = painterResource(id = R.drawable.salirblanco),
                contentDescription = "cerrar sesion",
                modifier = Modifier.size(30.dp)
            )
        }

        // Contenido principal centrado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a",
                style = TextStyle(fontFamily = PoppinsNormal),
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFA2C9FE),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 120.dp, bottom = 10.dp),
            )

            Text(
                text = " Tanemí ",
                style = TextStyle(fontFamily = Iansui),
                fontSize = 42.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(id = R.drawable.logocirculo),
                contentDescription = "logo tanemi",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "¿Cómo puedo ayudarte $userName?",
                style = TextStyle(fontFamily = PoppinsNormal),
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { navController.navigate("traductor") },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF8C52FF),
                                    Color(0xFF5CE1E6)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        ),
                    contentAlignment = Alignment.Center
                )
                {
                    Text("Traducir idioma", fontSize = 23.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(fontFamily = PoppinsBold),
                        color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    authViewModel.checkDeviceState(
                        onSuccess = { message ->
                            // Mostrar el mensaje de que se ha vinculado al otro dispositivo
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        },
                        onError = { error ->
                            // Mostrar el mensaje de que no se ha vinculado a otro dispositivo
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF8C52FF),
                                    Color(0xFF5CE1E6)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        ),
                    contentAlignment = Alignment.Center
                )
                {
                    Text("Vincular con smartwatch", fontSize = 23.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(fontFamily = PoppinsBold),
                        color = Color.White)
                }
            }
        }
    }
}
