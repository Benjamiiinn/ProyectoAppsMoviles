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
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.ui.theme.outlinedTextFieldColorsCustom
import com.example.proyectomoviles.viewmodel.ProductViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController, productViewModel: ProductViewModel = viewModel()) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var plataforma by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    // Variable para saber si el botón de guardar debe estar activo
    val isFormValid by derivedStateOf {
        nombre.isNotBlank() && precio.isNotBlank() && stock.isNotBlank() && plataforma.isNotBlank() && imagenUrl.isNotBlank() && descripcion.isNotBlank()
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = BackgroundDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            BorderStroke(1.dp, VaporWhiteBorder),
                            shape = CardDefaults.shape
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6F42C1)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Agregar Nuevo Producto",
                            style = MaterialTheme.typography.headlineMedium,
                            color = VaporWhiteBorder
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre del Producto") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = precio,
                            onValueChange = { precio = it.filter { c -> c.isDigit() || c == '.' } },
                            label = { Text("Precio") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it.filter { c -> c.isDigit() } },
                            label = { Text("Stock") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = plataforma,
                            onValueChange = { plataforma = it },
                            label = { Text("Plataforma (PC, PS5, etc.)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = imagenUrl,
                            onValueChange = { imagenUrl = it },
                            label = { Text("URL de la Imagen") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            colors = outlinedTextFieldColorsCustom()
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                val newProduct = Producto(
                                    id = Random.nextInt(1000, 9999), // ID aleatorio para simulación
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    precio = precio.toDoubleOrNull() ?: 0.0,
                                    stock = stock.toIntOrNull() ?: 0,
                                    plataforma = plataforma,
                                    imagenUrl = imagenUrl
                                )
                                productViewModel.addProduct(newProduct)
                                navController.popBackStack() // Volver a la pantalla de admin
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isFormValid, // El botón se activa cuando el formulario es válido
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VaporPink,
                                disabledContainerColor = VaporPink.copy(alpha = 0.5f)
                            )
                        ) {
                            Text("Guardar Producto")
                        }
                    }
                }
            }
        }
    }
}