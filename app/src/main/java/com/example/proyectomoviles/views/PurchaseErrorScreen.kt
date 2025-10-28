package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder

@Composable
fun PurchaseErrorScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error en la compra",
                modifier = Modifier.size(120.dp),
                tint = VaporPink
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "¡Error en la Compra!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = VaporWhiteBorder
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No se pudo procesar tu pedido. Es posible que uno de los productos se haya quedado sin stock mientras comprabas. Por favor, revisa tu carrito.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = VaporCyanText
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    // Vuelve a la pantalla del carrito para que el usuario pueda corregir el problema
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = VaporPink)
            ) {
                Text("Revisar Carrito")
            }
        }
    }
}
