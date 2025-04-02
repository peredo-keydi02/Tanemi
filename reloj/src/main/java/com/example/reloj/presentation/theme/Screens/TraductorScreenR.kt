package com.example.reloj.presentation.theme.Screens

import android.app.Activity
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Scaffold
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import com.example.reloj.R
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.PositionIndicator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.VignettePosition
import com.example.reloj.presentation.theme.Auth.AuthViewModelR
import com.example.reloj.presentation.theme.Iansui
import com.example.reloj.presentation.theme.PoppinsBold
import com.example.reloj.presentation.theme.PoppinsNormal
import com.example.reloj.presentation.theme.services.TextToSpeechHelper
import com.example.reloj.presentation.theme.services.googleTranslate
import kotlinx.coroutines.launch



@Composable
fun TraductorScreenR(navController: NavController, authViewModelR: AuthViewModelR) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var translatedText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()



    val listState = rememberScalingLazyListState()


    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondon),
                contentDescription = "Fondo de la pantalla",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.regresar),
                    contentDescription = "Volver"
                )
            }

            ScalingLazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Presiona el micrófono",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        style = TextStyle(fontFamily = Iansui)
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable {
                                showDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.microfono),
                            contentDescription = "Micrófono",
                            modifier = Modifier.size(40.dp)
                        )
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
                                    "Debido a que soy un emulador, no tengo soporte de audio, sin embargo, sigo siendo tu traductor de confianza.",
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
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
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
                    BasicTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (inputText.text.isEmpty()) {
                                Text(text = "Ingresa texto", color = Color.Gray)
                            }
                            innerTextField()
                        }
                    }
                }


                item{
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item{
                    Text(text = " $translatedText", fontSize = 16.sp)
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    Log.d("TraductorScreen", "Texto a traducir: ${inputText.text}")
                                    translatedText = googleTranslate(inputText.text, "es", "en") // Traduce de español a inglés
                                    Log.d("TraductorScreen", "Texto traducido: $translatedText")

                                    // Guardar en la base de datos la traducción
                                    authViewModelR.originalText.value = inputText.text // Guardar el texto original
                                    authViewModelR.translatedText.value =translatedText

                                    authViewModelR.saveTranslation(
                                        onSuccess = {
                                            // Si la traducción se guarda correctamente, puedes agregar una notificación o mensaje
                                            Toast.makeText(context, "Traducción guardada", Toast.LENGTH_SHORT).show()
                                        },
                                        onError = { error ->
                                            // Si ocurre un error al guardar la traducción
                                            Toast.makeText(context, "Error al guardar traducción: $error", Toast.LENGTH_SHORT).show()
                                        }
                                    )

                                    TextToSpeechHelper(context).speak(translatedText)
                                } catch (e: Exception) {
                                    Log.e("TraductorScreen", "Error en la traducción: ${e.message}")
                                    errorMessage = "Error en la traducción"
                                }
                            }
                        },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent), // ✅ Corrección
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(27.dp)
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
                            Text("Traducir", fontSize = 14.sp, color = Color.White, style = TextStyle(fontFamily = PoppinsBold),
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    IconButton(onClick = {navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.regresarblanco),
                            contentDescription = "regresar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}