package com.example.proyectomoviles.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporPurpleBorder
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiTopBar(
    title: String,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    // FIX: Accedemos directamente a la propiedad de estado, ya no es un Flow
    val cartItems = cartViewModel.cartItems

    TopAppBar(
        title = { Text(title, color = VaporWhiteBorder) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = VaporPurpleBorder
        ),
        actions = {
            IconButton(onClick = { navController.navigate("cart") }) {
                BadgedBox(
                    badge = {
                        if (cartItems.isNotEmpty()) {
                            Badge(containerColor = VaporPink) { Text(cartItems.size.toString(), color = Color.White) }
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = VaporCyanText
                    )
                }
            }
        }
    )
}
