package com.example.proyectomoviles.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.proyectomoviles.R
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.ui.theme.outlinedTextFieldColorsCustom
import com.example.proyectomoviles.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading
    val (messageText, isError) = viewModel.mensaje.value

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
            Image(
                painter = painterResource(id = R.drawable.vortex_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(150.dp)
                    .padding(bottom = 20.dp),
                contentScale = ContentScale.Fit
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(
                            width = 1.dp,
                            color = VaporWhiteBorder
                        ),
                        shape = CardDefaults.shape
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF6F42C1)
                ),
                shape = CardDefaults.shape
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Inicio de Sesión", style = MaterialTheme.typography.titleLarge.copy(color = VaporWhiteBorder))

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email, 
                        onValueChange = { email = it }, 
                        label = { Text("Email", style = labelTextStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = inputTextStyle,
                        colors = outlinedTextFieldColorsCustom(),
                        enabled = !isLoading
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

                            IconButton(onClick = {passwordVisible = !passwordVisible}){
                                Icon(imageVector  = image, description, tint = VaporCyanText)
                            }
                        },
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.login(email, password) { success ->
                                if (success) {
                                    navController.navigate("home/$email") {
                                        // Limpiamos la pila de navegación para que el usuario no pueda volver a la pantalla de login
                                        popUpTo(0)
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = VaporPink),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text("Entrar")
                        }
                    }

                    if (messageText.isNotEmpty()) {
                        Text(
                            text = messageText,
                            modifier = Modifier.padding(top = 10.dp),
                            color = if (isError) Color.Red else VaporCyanText
                        )
                    }
                    
                     TextButton(
                         onClick = { navController.navigate("register") },
                         enabled = !isLoading
                     ) {
                        Text("¿No tienes cuenta? Regístrate", color = VaporCyanText)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController(), viewModel = AuthViewModel())
}