package com.example.proyectomoviles.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember // FIX: Import faltante
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectomoviles.model.Order
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.OrdersViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    ordersViewModel: OrdersViewModel = viewModel()
) {
    val orders = ordersViewModel.orders
    val isLoading = ordersViewModel.isLoading
    val errorMessage = ordersViewModel.errorMessage
    val currentUserId = authViewModel.usuarioActual.value

    LaunchedEffect(currentUserId) {
        currentUserId?.let {
            ordersViewModel.loadOrders(it)
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos", color = VaporWhiteBorder) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = VaporPink)
            } else if (errorMessage.isNotEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = errorMessage,
                        color = VaporCyanText,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { currentUserId?.let { ordersViewModel.loadOrders(it) } }) {
                        Text("Reintentar")
                    }
                }
            } else if (orders.isEmpty()) {
                Text(
                    text = "No has realizado ningún pedido todavía.",
                    color = VaporCyanText,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(orders) { order ->
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
        border = BorderStroke(1.dp, VaporWhiteBorder),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6F42C1)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val formattedDate = remember(order.date) {
                SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("es", "ES")).format(order.date)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${order.id.take(8)}...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = VaporWhiteBorder
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = VaporCyanText
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            order.items.forEach {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("• ${it.quantity}x ", color = VaporCyanText)
                    Text(it.producto.nombre, color = VaporWhiteBorder, modifier = Modifier.weight(1f))
                    Text(formatPrice(it.producto.precio * it.quantity), color = VaporCyanText)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp), color = VaporWhiteBorder.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ",
                    style = MaterialTheme.typography.titleMedium,
                    color = VaporWhiteBorder
                )
                Text(
                    text = formatPrice(order.total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = VaporWhiteBorder
                )
            }
        }
    }
}
