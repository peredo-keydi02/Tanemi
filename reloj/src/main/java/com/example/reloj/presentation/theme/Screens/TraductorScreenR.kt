package com.example.reloj.presentation.theme.Screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.VignettePosition
import com.example.reloj.presentation.theme.Auth.AuthViewModelR
import com.example.reloj.presentation.theme.Iansui
import com.example.reloj.presentation.theme.PoppinsBold
import java.util.Locale

@Composable
fun TraductorScreenR(navController: NavController, authViewModelR: AuthViewModelR) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var translatedText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val listState = rememberScalingLazyListState() // ✅ Estado del scroll

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

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }, // ✅ Corrección
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) } // ✅ Corrección
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
                state = listState, // ✅ Estado agregado
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
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Di algo...")
                                }
                                speechRecognizerLauncher.launch(intent)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.microfono),
                            contentDescription = "Micrófono",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Button(
                        onClick = {/*
                            if (inputText.text.isNotBlank()) {
                                authViewModelR.originalText.value = inputText.text
                                translatedText = "Traducción: ${inputText.text}"
                                authViewModelR.translatedText.value = translatedText
                            } else {
                                errorMessage = "Ingresa una palabra"
                            }*/
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

                if (translatedText.isNotEmpty()) {
                    item {
                        Text(
                            text = translatedText,
                            fontSize = 12.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    item {
                        Text(
                            text = errorMessage,
                            fontSize = 12.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
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

fun cambios(){

}
