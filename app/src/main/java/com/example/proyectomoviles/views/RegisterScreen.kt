package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectomoviles.viewmodel.AuthViewModel
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.outlinedTextFieldColorsCustom

val VaporPink = Color(0xFFEA39B8)
val VaporInputBackground = Color(0xFF30115E) // Fondo que quieres para las casillas
val VaporCyanText = Color(0xFF32FBE2)      // Color que quieres para el texto dentro
val VaporPurpleBorder = Color(0xFF6F42C1) // Color para el borde
val VaporMutedText = Color(0xFFAAAAAA)
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }

    val inputTextStyle = TextStyle(
        fontFamily = FontFamily.Default, // Cambia si tienes una fuente custom
        fontSize = 16.sp,                // Tamaño del texto que escribes
        fontWeight = FontWeight.Normal
        // color = VaporCyanText --> Se define mejor en 'colors'
    )

    // Estilo de texto personalizado para las labels (opcional)
    val labelTextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp // Tamaño un poco más pequeño para el label
        // color = ... --> Se define en 'colors'
    )

    // 1. Envuelve todo en un Surface
    Surface(
        modifier = Modifier.fillMaxSize(), // Ocupa toda la pantalla
        color = BackgroundDark // 2. Aplica tu color de fondo aquí
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // El Column ahora puede llenar el Surface
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // El Card ahora estará SOBRE el fondo del Surface
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF6F42C1))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Registrate", style = MaterialTheme.typography.titleLarge.copy(color = VaporPink ))

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre", style = labelTextStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = inputTextStyle,
                        colors = outlinedTextFieldColorsCustom()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", style = labelTextStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = inputTextStyle,
                        colors = outlinedTextFieldColorsCustom()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", style = labelTextStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = inputTextStyle,
                        colors = outlinedTextFieldColorsCustom()
                    )
                    OutlinedTextField(
                        value = rut,
                        onValueChange = { rut = it },
                        label = { Text("RUT", style = labelTextStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = inputTextStyle,
                        colors = outlinedTextFieldColorsCustom()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (viewModel.registrar(nombre, email, password, rut)) {
                                navController.navigate("login")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VaporPink
                        )
                    ) {
                        Text("Registrar")
                    }

                    Text(viewModel.mensaje.value, modifier = Modifier.padding(top = 10.dp), color = VaporCyanText)

                    TextButton(onClick = { navController.navigate("login") }) {
                        Text("¿Ya tienes cuenta? Inicia sesión", color = VaporCyanText)
                    }
                }
            }
        }
    }
}

// --- (Preview no cambia fundamentalmente, pero ahora verá el Surface) ---
@Preview(showBackground = true) // showBackground = true solo pone un fondo blanco/negro genérico en el preview
@Composable
fun RegisterScreenPreview() {
    // Para ver tu color real en el Preview, aplica el Surface también aquí
    Surface(color = BackgroundDark) {
        RegisterScreen(navController = rememberNavController(), viewModel = AuthViewModel())
    }
}