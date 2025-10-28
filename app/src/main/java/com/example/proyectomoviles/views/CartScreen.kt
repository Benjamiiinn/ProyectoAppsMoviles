package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.viewmodel.CartViewModel

@Composable
fun CartScreen(cartViewModel: CartViewModel, navController: NavController) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val total = cartItems.sumOf { it.precio }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems, key = { it.id }) { item ->
                    CartItemRow(item = item, onRemove = { cartViewModel.removeFromCart(item) })
                    Divider()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(String.format("$%.2f", total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { 
                    cartViewModel.clearCart()
                    navController.navigate("confirmation")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar Compra")
            }
        }
    }
}

@Composable
fun CartItemRow(item: Producto, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.nombre, fontWeight = FontWeight.Bold)
            Text(String.format("$%.2f", item.precio))
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar del carrito")
        }
    }
}
