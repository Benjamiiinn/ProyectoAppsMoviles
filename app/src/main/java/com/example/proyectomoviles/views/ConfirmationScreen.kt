package com.example.proyectomoviles.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.CartViewModel
import java.util.UUID

@Composable
fun ConfirmationScreen(navController: NavController, cartViewModel: CartViewModel) {
    val orderItems = cartViewModel.lastSuccessfulOrder
    // CORREGIDO: Hacemos el cálculo en Double para que coincida con formatPrice
    val total = orderItems.sumOf { it.producto.precio.toDouble() * it.quantity }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Compra exitosa",
                modifier = Modifier.size(80.dp),
                tint = VaporCyanText
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Gracias por tu compra!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = VaporWhiteBorder
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tus códigos de descarga están listos.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = VaporCyanText
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- La Boleta ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, VaporWhiteBorder),
                colors = CardDefaults.cardColors(containerColor = BackgroundDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen de tu Compra", style = MaterialTheme.typography.titleLarge, color = VaporWhiteBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                        items(orderItems) { item ->
                            val downloadCode = remember(item.producto.id) {
                                // Generamos un código de descarga falso y único
                                "${item.producto.nombre.take(3).uppercase()}-${UUID.randomUUID().toString().take(8).uppercase()}"
                            }
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(item.producto.nombre, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
                                Text("Código de descarga:", fontSize = 12.sp, color = VaporCyanText)
                                Text(downloadCode, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = VaporPink)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            HorizontalDivider(color = VaporWhiteBorder.copy(alpha = 0.3f)) // Corregido: Renombrado a HorizontalDivider
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Pagado: ", style = MaterialTheme.typography.titleMedium, color = VaporWhiteBorder)
                        Text(formatPrice(total), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { 
                    navController.navigate("home/${cartViewModel.currentUserId}") { // Asumimos que currentUserId está disponible
                        popUpTo("home/${cartViewModel.currentUserId}") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = VaporPink)
            ) {
                Text("Volver al Inicio")
            }
        }
    }
}
