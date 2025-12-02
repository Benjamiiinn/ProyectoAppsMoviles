package com.example.proyectomoviles.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.proyectomoviles.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading
    val (messageText, isError) = viewModel.mensaje.value

    val customTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = VaporPink,
        unfocusedBorderColor = VaporWhiteBorder,
        focusedLabelColor = VaporPink,
        unfocusedLabelColor = VaporWhiteBorder,
        cursorColor = VaporCyanText,
        focusedTextColor = VaporWhiteBorder,
        unfocusedTextColor = VaporWhiteBorder
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.vortex_logo),
                contentDescription = "Logo",
                modifier = Modifier.height(120.dp).padding(bottom = 20.dp),
                contentScale = ContentScale.Fit
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, VaporWhiteBorder), shape = CardDefaults.shape),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6F42C1)),
                shape = CardDefaults.shape
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Regístrate", style = MaterialTheme.typography.titleLarge.copy(color = VaporWhiteBorder))
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = customTextFieldColors, enabled = !isLoading)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), colors = customTextFieldColors, enabled = !isLoading)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = customTextFieldColors,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, null, tint = VaporCyanText)
                            }
                        },
                        enabled = !isLoading
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("RUT") }, modifier = Modifier.fillMaxWidth(), colors = customTextFieldColors, enabled = !isLoading)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), colors = customTextFieldColors, enabled = !isLoading)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth(), colors = customTextFieldColors, enabled = !isLoading)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.registrar(nombre, email, password, rut, telefono, direccion) { success ->
                                if (success) {
                                    navController.popBackStack()
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
                            Text("Registrar")
                        }
                    }

                    if (messageText.isNotEmpty()) {
                        Text(text = messageText, modifier = Modifier.padding(top = 10.dp), color = if (isError) Color.Red else VaporCyanText)
                    }

                    TextButton(onClick = { navController.popBackStack() }, enabled = !isLoading) {
                        Text("¿Ya tienes cuenta? Inicia sesión", color = VaporCyanText)
                    }
                }
            }
        }
    }
}
