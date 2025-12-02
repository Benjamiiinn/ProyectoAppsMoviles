package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.ui.theme.*
import com.example.proyectomoviles.viewmodel.ProductViewModel
//abeja
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(productViewModel: ProductViewModel = viewModel(), navController: NavController) {
    val productos = productViewModel.productos

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administrador", color = VaporWhiteBorder) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = VaporPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addProduct") },
                containerColor = VaporPink
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto", tint = Color.White)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Gestión de Productos", 
                    style = MaterialTheme.typography.titleLarge, 
                    modifier = Modifier.padding(top=16.dp),
                    color = VaporWhiteBorder
                )
            }
            items(productos) { producto ->
                AdminProductRow(producto = producto, onStockChange = {
                    productViewModel.updateStock(producto.id, it)
                })
                Divider(color = VaporWhiteBorder.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun AdminProductRow(producto: Producto, onStockChange: (Int) -> Unit) {
    var newStock by remember { mutableStateOf(producto.stock.toString()) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            producto.nombre, 
            modifier = Modifier.weight(1f),
            color = VaporCyanText
        )
        OutlinedTextField(
            value = newStock,
            onValueChange = { newStock = it.filter { it.isDigit() } },
            label = { Text("Stock") },
            modifier = Modifier.width(120.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = outlinedTextFieldColorsCustom(),
            trailingIcon = {
                Button(
                    onClick = { 
                        val stockInt = newStock.toIntOrNull() ?: producto.stock
                        onStockChange(stockInt)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VaporPink),
                    contentPadding = PaddingValues(horizontal = 8.dp) 
                ) {
                    Text("OK")
                }
            }
        )
    }
}