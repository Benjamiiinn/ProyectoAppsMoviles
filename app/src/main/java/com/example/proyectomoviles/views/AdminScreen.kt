package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.viewmodel.ProductViewModel

@Composable
fun AdminScreen(productViewModel: ProductViewModel = viewModel()) {
    val productos = productViewModel.productos

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Panel de Administración", style = MaterialTheme.typography.titleLarge)
        }
        items(productos) { producto ->
            AdminProductRow(producto = producto, onStockChange = {
                productViewModel.updateStock(producto.id, it)
            })
            Divider()
        }
    }
}

@Composable
fun AdminProductRow(producto: Producto, onStockChange: (Int) -> Unit) {
    var newStock by remember { mutableStateOf(producto.stock.toString()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(producto.nombre, modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = newStock,
            onValueChange = { newStock = it },
            label = { Text("Stock") },
            modifier = Modifier.width(120.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = {
                Button(onClick = { 
                    val stockInt = newStock.toIntOrNull() ?: producto.stock
                    onStockChange(stockInt)
                }) {
                    Text("OK")
                }
            }
        )
    }
}