package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectomoviles.model.Order
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    ordersViewModel: OrdersViewModel
) {
    val currentUserEmail = authViewModel.usuarioActual.value?.email

    LaunchedEffect(currentUserEmail) {
        if (currentUserEmail != null) {
            ordersViewModel.fetchOrders(currentUserEmail)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
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
                    CircularProgressIndicator()
                }
            } else if (ordersViewModel.errorMessage.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(ordersViewModel.errorMessage, color = MaterialTheme.colorScheme.error)
                }
            } else if (ordersViewModel.orders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes pedidos realizados.")
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
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pedido #${order.id}", fontWeight = FontWeight.Bold)
            Text("Fecha: ${order.date}")
            Spacer(modifier = Modifier.height(8.dp))
            order.items.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${item.quantity}x ${item.producto.nombre}")
                    // CORREGIDO: Convertimos el resultado a Double antes de formatearlo
                    Text(formatPrice((item.producto.precio * item.quantity).toDouble()))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Total: ${formatPrice(order.total)}", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.End))
        }
    }
}
