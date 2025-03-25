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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.auth.AuthViewModel

@Composable
fun RecuperarContrasena(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
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

            Text(text = "¿No recuerdas tu contraseña?", fontSize = 27.sp, color = Color(0xFF6C63FF))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(140.dp)  // Cuadro azul más grande
                    .background(Color(0xFFD2EBFF), shape = RoundedCornerShape(15.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(15.dp))
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
                        navController.navigate("modificarcontrasena")
                    } else {
                        errorMessage = "Por favor, ingresa tu correo electrónico."
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier
                    .padding(top = 15.dp)
                    .border(2.dp, Color(0xFF6C63FF), RoundedCornerShape(20.dp))
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(text = "Recuperar", fontSize = 18.sp, color = Color(0xFF6C63FF))
            }
        }
    }
}

@Composable
fun RecuperarInputField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Email,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 18.sp, color = Color.Black) // Texto en negro
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 14.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

