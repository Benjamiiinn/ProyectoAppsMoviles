package com.example.proyectomoviles.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectomoviles.model.Genero
import com.example.proyectomoviles.model.Plataforma
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.ui.theme.outlinedTextFieldColorsCustom
import com.example.proyectomoviles.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController, productViewModel: ProductViewModel = viewModel()) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenBase64 by remember { mutableStateOf("") } // Cambio de nombre para claridad
    var plataformaId by remember { mutableStateOf("") } // Ahora pedimos ID
    var generoId by remember { mutableStateOf("") }     // Ahora pedimos ID
    var stock by remember { mutableStateOf("") }

    val isFormValid by remember {
        derivedStateOf {
            nombre.isNotBlank() &&
                    precio.isNotBlank() &&
                    stock.isNotBlank() &&
                    plataformaId.isNotBlank() &&
                    generoId.isNotBlank()
        }
    }


    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto", color = VaporWhiteBorder) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = VaporPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize().padding(paddingValues), color = BackgroundDark) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
                Card(
                    modifier = Modifier.fillMaxWidth().border(BorderStroke(1.dp, VaporWhiteBorder), shape = CardDefaults.shape),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6F42C1))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nuevo Juego", style = MaterialTheme.typography.headlineMedium, color = VaporWhiteBorder)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = precio, onValueChange = { precio = it.filter { c -> c.isDigit() } },
                            label = { Text("Precio (Entero)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = stock, onValueChange = { stock = it.filter { c -> c.isDigit() } },
                            label = { Text("Stock") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // CAMBIO: Pedimos ID para Platforma y Género
                        OutlinedTextField(
                            value = plataformaId, onValueChange = { plataformaId = it.filter { c -> c.isDigit() } },
                            label = { Text("ID Plataforma (ej: 1)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = generoId, onValueChange = { generoId = it.filter { c -> c.isDigit() } },
                            label = { Text("ID Género (ej: 1)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Nota: Aquí deberías implementar un selector de imagen real. Por ahora es un texto.
                        OutlinedTextField(
                            value = imagenBase64, onValueChange = { imagenBase64 = it },
                            label = { Text("Base64 Imagen (Texto Largo)") },
                            modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth().height(100.dp), colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                // Construimos el objeto con los datos
                                val newProduct = Producto(
                                    id = 0, // El backend asigna el ID
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    precio = precio.toIntOrNull() ?: 0,
                                    stock = stock.toIntOrNull() ?: 0,
                                    imagen = imagenBase64, // Enviamos el string base64
                                    // Creamos objetos dummy con el ID correcto para que el ViewModel lo procese
                                    plataforma = Plataforma(id = plataformaId.toIntOrNull() ?: 0, nombre = ""),
                                    genero = Genero(id = generoId.toIntOrNull() ?: 0, nombre = "")
                                )
                                productViewModel.addProduct(newProduct) { success ->
                                    if(success) navController.popBackStack()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isFormValid,
                            colors = ButtonDefaults.buttonColors(containerColor = VaporPink)
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}