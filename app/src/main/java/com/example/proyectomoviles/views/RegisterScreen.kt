package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.outlinedTextFieldColorsCustom
import com.example.proyectomoviles.viewmodel.AuthViewModel

val VaporPink = Color(0xFFEA39B8)
val VaporCyanText = Color(0xFF32FBE2)

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val inputTextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )

    val labelTextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF6F42C1)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Registrate", style = MaterialTheme.typography.titleLarge.copy(color = VaporPink))

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
                        colors = outlinedTextFieldColorsCustom(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, description, tint = VaporCyanText)
                            }
                        }
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
                        colors = ButtonDefaults.buttonColors(containerColor = VaporPink)
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

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    Surface(color = BackgroundDark) {
        RegisterScreen(navController = rememberNavController(), viewModel = AuthViewModel())
    }
}
