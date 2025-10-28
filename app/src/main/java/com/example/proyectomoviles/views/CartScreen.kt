package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.CartViewModel

@Composable
fun CartScreen(cartViewModel: CartViewModel, navController: NavController) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val total = cartItems.sumOf { it.producto.precio * it.quantity }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge, color = VaporCyanText)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems, key = { it.producto.id }) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { cartViewModel.increaseQuantity(item.producto.id) },
                            onDecrease = { cartViewModel.decreaseQuantity(item.producto.id) },
                            onRemove = { cartViewModel.removeFromCart(item.producto.id) }
                        )
                        Divider(color = VaporWhiteBorder.copy(alpha = 0.5f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total:", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
                    Text(formatPrice(total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("payment") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VaporPink)
                ) {
                    Text("Proceder al Pago")
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(0.4f)) {
            Text(item.producto.nombre, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
            Text(formatPrice(item.producto.precio), color = VaporCyanText)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.5f)
        ) {
            IconButton(onClick = onDecrease, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Remove, contentDescription = "Restar uno", tint = VaporCyanText)
            }
            Text(
                text = item.quantity.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = VaporWhiteBorder
            )
            IconButton(onClick = onIncrease, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Añadir uno", tint = VaporCyanText)
            }
        }

        IconButton(onClick = onRemove, modifier = Modifier.weight(0.1f)) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar del carrito", tint = VaporPink)
        }
    }
}
