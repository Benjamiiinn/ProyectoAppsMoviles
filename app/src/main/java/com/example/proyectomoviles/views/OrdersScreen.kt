package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectomoviles.model.Order
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    authViewModel: AuthViewModel, // Mantenemos el argumento aunque no lo usemos directamente aquí
    ordersViewModel: OrdersViewModel
) {
    // 1. CORRECCIÓN: fetchOrders() no necesita parámetros, el token ya tiene la info.
    // Usamos LaunchedEffect para cargar los pedidos al entrar a la pantalla.
    LaunchedEffect(Unit) {
        ordersViewModel.fetchOrders()
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos", color = VaporWhiteBorder) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = VaporPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (ordersViewModel.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VaporPink)
                }
            } else if (ordersViewModel.errorMessage.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(ordersViewModel.errorMessage, color = MaterialTheme.colorScheme.error)
                }
            } else if (ordersViewModel.orders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes pedidos realizados.", color = VaporCyanText)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(ordersViewModel.orders) { order ->
                        OrderCard(order = order)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pedido #${order.id}", fontWeight = FontWeight.Bold, color = VaporPink)

            // 2. CORRECCIÓN: Usamos 'fecha' en lugar de 'date' (según Order.kt)
            Text("Fecha: ${order.fecha}", color = VaporWhiteBorder)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) // Línea separadora

            order.items.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    // 3. CORRECCIÓN: Usamos 'cantidad' en lugar de 'quantity'
                    Text("${item.cantidad}x ${item.producto.nombre}", color = VaporCyanText)

                    // Cálculo del subtotal por item
                    val subtotal = item.producto.precio * item.cantidad
                    Text(formatPrice(subtotal.toDouble()), color = VaporWhiteBorder)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. CORRECCIÓN: Convertimos 'total' a Double para formatearlo
            Text(
                text = "Total: ${formatPrice(order.total.toDouble())}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End),
                color = VaporPink
            )
        }
    }
}