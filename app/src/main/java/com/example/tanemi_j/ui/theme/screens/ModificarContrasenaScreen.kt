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
import com.example.tanemi_j.R
import com.example.tanemi_j.ui.theme.auth.AuthViewModel

@Composable
fun ModificarContrasenaScreen (navController: NavController, authViewModel: AuthViewModel) {
    var contrasena by remember { mutableStateOf("") }
    var confirmarcontrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC2E8FF))
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { navController.navigate("recuperarcontrasena") },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.regresar),
                contentDescription = "regresar",
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFC2E8FF)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Cambiar contraseña", fontSize = 32.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(Color(0xFF90CAF9), shape = RoundedCornerShape(10.dp))
                    .border(3.dp, Color.White, RoundedCornerShape(10.dp))
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
                colors = ButtonDefaults.buttonColors(Color(0xFF42A5F5)),
                modifier = Modifier.padding(top = 15.dp)
            ) {
                Text(text = "Confirmar", fontSize = 22.sp)
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
