package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.CartViewModel

@Composable
fun CartScreen(authViewModel: AuthViewModel, cartViewModel: CartViewModel, navController: NavController) {
    val cartItems = cartViewModel.cartItems

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemView(item = item, cartViewModel = cartViewModel)
                    HorizontalDivider()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                 Text(
                    text = "Total: ${formatPrice(cartViewModel.getTotalPrice())}",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { 
                        navController.navigate("payment") 
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = cartItems.isNotEmpty()
                ) {
                    Text("Proceder al Pago")
                }
            }
        }
    }
}

@Composable
fun CartItemView(item: CartItem, cartViewModel: CartViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        
        Column(modifier = Modifier.weight(1f)) {
            Text(item.producto.nombre, fontWeight = FontWeight.Bold)
            // CORREGIDO: Convertimos el precio (Int) a Double antes de formatearlo
            Text(formatPrice(item.producto.precio.toDouble()))
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { cartViewModel.decreaseQuantity(item.producto.id) }) {
                Text("-", fontSize = 20.sp)
            }
            Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { cartViewModel.increaseQuantity(item.producto.id) }) {
                Text("+", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { cartViewModel.removeFromCart(item.producto.id) }) {
                Text("❌", color = Color.Red)
            }
        }
    }
}
