package com.example.proyectomoviles.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
// CORREGIDO: Se importa la versión AutoMirrored del icono
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.proyectomoviles.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController, productViewModel: ProductViewModel = viewModel()) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    var selectedGenero by remember { mutableStateOf<Genero?>(null) }
    var selectedPlataforma by remember { mutableStateOf<Plataforma?>(null) }

    val generos = productViewModel.generos
    val plataformas = productViewModel.plataformas

    val isFormValid by derivedStateOf {
        nombre.isNotBlank() && precio.isNotBlank() && stock.isNotBlank() &&
        descripcion.isNotBlank() && imagenUrl.isNotBlank() &&
        selectedGenero != null && selectedPlataforma != null
    }

    val customTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = VaporPink,
        unfocusedBorderColor = VaporWhiteBorder,
        focusedLabelColor = VaporPink,
        unfocusedLabelColor = VaporWhiteBorder,
        cursorColor = Color(0xFF32FBE2), // VaporCyanText
        focusedTextColor = VaporWhiteBorder,
        unfocusedTextColor = VaporWhiteBorder
    )

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto", color = VaporWhiteBorder) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // CORREGIDO: Se usa el icono AutoMirrored
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = VaporPink)
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
                            "Nuevo Producto",
                            style = MaterialTheme.typography.headlineMedium,
                            color = VaporWhiteBorder
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre del Producto") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilterDropdown(generos, selectedGenero, { selectedGenero = it }, "Género", { it.nombre })
                        Spacer(modifier = Modifier.height(8.dp))

                        FilterDropdown(plataformas, selectedPlataforma, { selectedPlataforma = it }, "Plataforma", { it.nombre })
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = precio,
                            onValueChange = { precio = it.filter { c -> c.isDigit() } },
                            label = { Text("Precio") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it.filter { c -> c.isDigit() } },
                            label = { Text("Stock") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = imagenUrl,
                            onValueChange = { imagenUrl = it },
                            label = { Text("URL de la Imagen") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                val newProduct = Producto(
                                    id = 0, 
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    precio = precio.toIntOrNull() ?: 0, 
                                    stock = stock.toIntOrNull() ?: 0,
                                    genero = selectedGenero!!,
                                    plataforma = selectedPlataforma!!,
                                    imagenUrl = imagenUrl
                                )
                                productViewModel.addProduct(newProduct) { success ->
                                    if (success) {
                                        navController.popBackStack()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isFormValid,
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
