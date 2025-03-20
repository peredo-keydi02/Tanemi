package com.example.tanemi_j.ui.theme.screens

import android.graphics.Paint.Align
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.auth.AuthViewModel


@Composable
fun MenuScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val userName by authViewModel.userName.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.fetchUserName() // Obtener el nombre al entrar en la pantalla
    }

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
                .padding(start = 30.dp, top = 35.dp)
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
                style = MaterialTheme.typography.titleSmall,
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFA2C9FE),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 120.dp, bottom = 10.dp),
            )

            Text(
                text = " Tanemí ",
                style = MaterialTheme.typography.bodyLarge,
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
                style = MaterialTheme.typography.titleSmall,
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
                    .padding(horizontal = 10.dp)
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
                    Text("Traducir idioma", fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
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
                    Text("Vincular con smartwatch", fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge, color = Color.White)
                }
            }
        }
    }
}
