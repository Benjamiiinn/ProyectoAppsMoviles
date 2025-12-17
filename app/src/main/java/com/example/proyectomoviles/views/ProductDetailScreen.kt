package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.CartViewModel
import com.example.proyectomoviles.viewmodel.ProductViewModel

@Composable
fun ProductDetailScreen(
    productId: Int,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    // Buscar el producto directamente en la lista del ViewModel
    val producto = remember(productId, productViewModel.productos) {
        productViewModel.productos.find { it.id == productId }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (producto == null) {
            // Muestra un indicador de carga o un mensaje de no encontrado
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // Si el productViewModel está cargando, muestra el indicador.
                // Si no, y el producto sigue siendo nulo, significa que no se encontró.
                if (productViewModel.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Producto no encontrado.", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            // Una vez encontrado el producto, mostramos los detalles
            val p = producto!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = p.imagen,
                    contentDescription = "Imagen de ${p.nombre}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(p.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(p.descripcion, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Plataforma: ${p.plataforma?.nombre ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                        Text("Género: ${p.genero?.nombre ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    // CORREGIDO: Convertimos el precio (Int) a Double antes de formatearlo
                    Text("Precio: ${formatPrice(p.precio.toDouble())}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Stock disponible: ${p.stock}", style = MaterialTheme.typography.bodyMedium)
                    
                    Spacer(modifier = Modifier.weight(1.0f)) // Empuja el botón hacia abajo

                    Button(
                        onClick = { 
                            cartViewModel.addToCart(p)
                            // Opcional: Navegar al carrito o mostrar un Snackbar
                            navController.navigate("cart")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = p.stock > 0
                    ) {
                        Text(if (p.stock > 0) "Añadir al Carrito" else "Sin Stock")
                    }
                }
            }
        }
    }
}
