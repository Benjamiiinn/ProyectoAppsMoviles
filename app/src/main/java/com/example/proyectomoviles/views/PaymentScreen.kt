package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(cartViewModel: CartViewModel, navController: NavController) {
    val cartItems = cartViewModel.cartItems
    val isLoading = cartViewModel.isLoading
    // CORREGIDO: Hacemos el cálculo en Double para que coincida con formatPrice
    val total = cartItems.sumOf { it.producto.precio.toDouble() * it.quantity }

    val paymentOptions = listOf("Tarjeta de Crédito/Débito", "PayPal", "Transferencia Bancaria")
    var selectedOption by remember { mutableStateOf(paymentOptions[0]) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Proceso de Pago", color = VaporWhiteBorder) },
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
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

                HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = VaporWhiteBorder.copy(alpha = 0.5f))

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
                            ),
                            enabled = !isLoading
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
                        cartViewModel.checkout { success ->
                            if (success) {
                                navController.navigate("confirmation") {
                                    popUpTo("home/{email}") { 
                                        inclusive = false 
                                    }
                                }
                            } else {
                                navController.navigate("purchaseError")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = cartItems.isNotEmpty() && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VaporPink,
                        disabledContainerColor = VaporPink.copy(alpha = 0.5f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Pagar " + formatPrice(total))
                    }
                }
            }
        }
    }
}
