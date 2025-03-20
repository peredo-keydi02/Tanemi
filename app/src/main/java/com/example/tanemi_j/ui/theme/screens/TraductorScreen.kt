package com.example.tanemi_j.ui.theme.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tanemi_j.ui.theme.auth.AuthViewModel
import com.example.tanemi_j.R // Necesario para los recursos como imágenes
import java.util.Locale


@Composable
fun TraductorScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var translatedText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val spokenText = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                if (spokenText != null) {
                    inputText = TextFieldValue(spokenText)
                }
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondosinlogo),
            contentDescription = "Fondo de la pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Botón de retroceso en la esquina superior izquierda
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.regresar),
                contentDescription = "Volver",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Escribe o usa el micrófono",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 10.dp)
            )

//            // **TextField de Material Design para ingresar texto manualmente**
//            OutlinedTextField(
//                value = inputText,
//                onValueChange = { inputText = it },
//                label = { Text("Ingresa texto") },
//                textStyle = LocalTextStyle.current.copy(color = Color.White),
//                singleLine = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 20.dp),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = Color.White,
//                    unfocusedBorderColor = Color.LightGray,
//                    cursorColor = Color.White,
//                    //TextColor = Color.White
//                )
//            )

            Spacer(modifier = Modifier.height(10.dp))

            // **Ícono de micrófono con reconocimiento de voz**
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Di algo...")
                        }
                        speechRecognizerLauncher.launch(intent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.microfono),
                    contentDescription = "Micrófono",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // **Botón para traducir**
            ElevatedButton(
                onClick = {
                    if (inputText.text.isNotBlank()) {
                        authViewModel.originalText.value = inputText.text
                        translatedText = "Traducción simulada de: ${inputText.text}"
                        authViewModel.translatedText.value = translatedText
                    } else {
                        errorMessage = "Ingresa una palabra para traducir"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Text("Traducir", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (translatedText.isNotEmpty()) {
                Text(
                    text = "Traducción: $translatedText",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    fontSize = 18.sp,
                    color = Color.Red
                )
            }
        }
    }
}
