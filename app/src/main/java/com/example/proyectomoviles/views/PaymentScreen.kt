package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.CartViewModel

@Composable
fun PaymentScreen(cartViewModel: CartViewModel, navController: NavController) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val total = cartItems.sumOf { it.producto.precio * it.quantity }

    val paymentOptions = listOf("Tarjeta de Crédito/Débito", "PayPal", "Transferencia Bancaria")
    var selectedOption by remember { mutableStateOf(paymentOptions[0]) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Resumen del Pedido", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total a Pagar:", style = MaterialTheme.typography.titleLarge, color = VaporWhiteBorder)
                Text(formatPrice(total), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
            }

            Divider(modifier = Modifier.padding(vertical = 24.dp), color = VaporWhiteBorder.copy(alpha = 0.5f))

            Text("Selecciona tu Método de Pago", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
            Spacer(modifier = Modifier.height(16.dp))

            paymentOptions.forEach { option ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (option == selectedOption),
                            onClick = { selectedOption = option }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = { selectedOption = option },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = VaporPink,
                            unselectedColor = VaporWhiteBorder
                        )
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp),
                        color = VaporCyanText
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (cartViewModel.checkout()) {
                        navController.navigate("confirmation") {
                            popUpTo("home/{email}") { 
                                inclusive = false 
                            }
                        }
                    } else {
                        navController.navigate("purchaseError")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = cartItems.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VaporPink,
                    disabledContainerColor = VaporPink.copy(alpha = 0.5f)
                )
            ) {
                Text("Pagar " + formatPrice(total))
            }
        }
    }
}