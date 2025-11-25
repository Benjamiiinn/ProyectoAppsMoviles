package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // FIX: Import faltante
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(authViewModel: AuthViewModel, cartViewModel: CartViewModel, navController: NavController) {
    val cartItems = cartViewModel.cartItems
    val isLoading = cartViewModel.isLoading
    val errorMessage = cartViewModel.errorMessage
    val total = cartItems.sumOf { it.producto.precio * it.quantity }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Cargamos el carrito para el usuario actual cuando la pantalla se muestra
    LaunchedEffect(authViewModel.usuarioActual.value) {
        authViewModel.usuarioActual.value?.let {
            cartViewModel.loadCart(it)
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras", color = VaporWhiteBorder) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Manejador de estados de la UI
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = VaporPink)
                } else if (errorMessage.isNotEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = errorMessage,
                            color = VaporCyanText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { authViewModel.usuarioActual.value?.let { cartViewModel.loadCart(it) } },
                            colors = ButtonDefaults.buttonColors(containerColor = VaporPink)
                        ) {
                            Text("Reintentar")
                        }
                    }
                } else if (cartItems.isEmpty()) {
                    Text(
                        text = "Tu carrito está vacío.",
                        color = VaporCyanText,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    // Contenido del carrito cuando la carga es exitosa
                    Column(Modifier.fillMaxSize()) {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(cartItems, key = { it.producto.id }) { item ->
                                CartItemRow(
                                    item = item,
                                    onIncrease = {
                                        cartViewModel.increaseQuantity(item.producto.id)
                                        scope.launch { snackbarHostState.showSnackbar("Cantidad actualizada") }
                                    },
                                    onDecrease = {
                                        cartViewModel.decreaseQuantity(item.producto.id)
                                        scope.launch { snackbarHostState.showSnackbar("Cantidad actualizada") }
                                    },
                                    onRemove = {
                                        cartViewModel.removeFromCart(item.producto.id)
                                        scope.launch { snackbarHostState.showSnackbar("Producto eliminado") }
                                    }
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
            IconButton(onClick = onDecrease, modifier = Modifier.size(36.dp), enabled = item.quantity > 1) {
                Icon(Icons.Default.Remove, contentDescription = "Restar uno", tint = if(item.quantity > 1) VaporCyanText else Color.Gray)
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
