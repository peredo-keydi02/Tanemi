package com.example.tanemi_j.ui.theme.screens

import android.app.ActivityManager
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.ResourceFont
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat.Style
import androidx.navigation.NavController
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.Acme
import com.example.tanemi_j.ui.theme.auth.AuthViewModel

@Composable
fun ModificarContrasenaScreen (navController: NavController, authViewModel: AuthViewModel) {
    var contrasena by remember { mutableStateOf("") }
    var confirmarcontrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = { navController.navigate("recuperarcontrasena") },
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Cambiar contraseña", fontSize = 32.sp, style = TextStyle(fontFamily = Acme), fontWeight = FontWeight.Bold ,color = Color(0xFF5B83E7), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logocirculo),
                contentDescription = "Logo tanemi",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(Color(0xFFC2E8FF), shape = RoundedCornerShape(10.dp))
                    .padding(20.dp)
            ) {
                Column {
                    ModificarInputField("Ingresa tu nueva contraseña", contrasena) { contrasena = it }
                    ModificarInputField("Confirma tu contraseña", confirmarcontrasena) { confirmarcontrasena = it }

                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
            }

            Button(
                onClick = {
                    when{
                        contrasena.isBlank() || confirmarcontrasena.isBlank()->
                            errorMessage = "Ambos campos deben ser llanados."
                        contrasena.length < 8 && confirmarcontrasena.length < 8 ->
                            errorMessage = "Ambas contrasenas deben tener al menos 8 carateres mínimo."
                        contrasena != confirmarcontrasena ->
                            errorMessage = "Las contraseñas no coinciden"
                        else ->
                            navController.navigate("login")
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(50.dp)
                    .width(200.dp)
                    .border(3.dp, Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))), RoundedCornerShape(50.dp)), // Apply gradient border
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Text(text = "Confirmar", fontSize = 25.sp, fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.titleMedium.copy(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF8A2BE2), Color(0xFF00BFFF))
                        )
                    )
                )
            }

        }
    }

}

@Composable
fun ModificarInputField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Normal, fontSize = 22.sp, color = Color(0xFF1B72B3), modifier = Modifier.padding(top = 10.dp))
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
