package com.example.reloj.presentation.theme.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.example.reloj.R
import com.example.reloj.presentation.theme.Auth.AuthViewModelR
import com.example.reloj.presentation.theme.Iansui
import com.example.reloj.presentation.theme.PoppinsBold
import com.example.reloj.presentation.theme.PoppinsNormal

@Composable
fun MenuScreenR(navController: NavController, authViewModelR: AuthViewModelR) {
    val userName by authViewModelR.userName.collectAsState()
    val listState = rememberScalingLazyListState() // ✅ Definir el estado de la lista

    LaunchedEffect(Unit) {
        authViewModelR.fetchUserName()
    }


    // Obtener el contexto de Compose
    val context = LocalContext.current

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
                contentDescription = "Fondo de pantalla",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

<<<<<<< HEAD
// keydi-reloj
            //lloremos juntos
=======
>>>>>>> 3fe22432e0212fa6671a8917903335553fe9ad9a
            IconButton(
                onClick = {
                    authViewModelR.logoutUser()
                    navController.navigate("login") {
                        popUpTo("menu") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.salirblanco),
                    contentDescription = "Cerrar sesión"
                )
            }

            ScalingLazyColumn(
                state = listState, // ✅ Pasar el estado
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Bienvenido a",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFA2C9FE),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontFamily = PoppinsNormal)
                    )
                }

                item {
                    Text(
                        text = "Tanemí",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontFamily = Iansui)
                    )
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.logocirculo),
                        contentDescription = "Logo Tanemí",
                        modifier = Modifier.size(70.dp)
                    )
                }

                item {
                    Text(
                        text = "¿Cómo te ayudo, $userName?",
                        fontSize = 14.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontFamily = PoppinsNormal),
                        fontWeight = FontWeight.Normal
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Button(
                        onClick = { navController.navigate("traductor") },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent), // ✅ Corrección
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(27.dp)
                    )
                    {
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
                        ) {
                            Text("Traducir idioma", fontSize = 14.sp, color = Color.White, style = TextStyle(fontFamily = PoppinsBold),
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Button(
                        onClick = {
                            authViewModelR.checkDeviceState(
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
                        ) {
                            Text("Vincular teléfono", fontSize = 14.sp, color = Color.White, style = TextStyle(fontFamily = PoppinsBold),
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    IconButton(onClick = {
                        authViewModelR.logoutUser()
                        navController.navigate("login") {
                            popUpTo("menu") { inclusive = true }
                        }

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.salirblanco),
                            contentDescription = "Cerrar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
<<<<<<< HEAD
 //keydi-reloj
=======

>>>>>>> 3fe22432e0212fa6671a8917903335553fe9ad9a

