package com.example.proyectomoviles.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.outlinedTextFieldColorsCustom
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.ProductViewModel

val VaporPink = Color(0xFFEA39B8)
val VaporCyanText = Color(0xFF32FBE2)
val VaporWhiteBorder = Color(0xFFDEE2E6)

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel = viewModel(),
    navController: NavController
) {
    val productos = productViewModel.productos
    val isLoading = productViewModel.isLoading
    val errorMessage = productViewModel.errorMessage

    var searchQuery by remember { mutableStateOf("") }

    // La lógica de filtrado ahora solo depende de la búsqueda por texto
    val filteredProductos = remember(searchQuery, productos) {
        if (searchQuery.isBlank()) {
            productos
        } else {
            productos.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bienvenido, ${authViewModel.usuarioActual.value}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    color = VaporCyanText
                )
                Button(
                    onClick = { navController.navigate("orders") },
                    colors = ButtonDefaults.buttonColors(containerColor = VaporCyanText),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Mis Pedidos", color = BackgroundDark)
                }
                if (authViewModel.isAdmin()) {
                    Button(
                        onClick = { navController.navigate("admin") },
                        colors = ButtonDefaults.buttonColors(containerColor = VaporPink),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Administrar")
                    }
                }
            }

            Text(
                text = "Catálogo de Juegos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                color = VaporWhiteBorder
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text("Buscar juego...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                colors = outlinedTextFieldColorsCustom(),
                singleLine = true
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = VaporPink)
                } else if (errorMessage.isNotEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = errorMessage, color = VaporCyanText, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { productViewModel.fetchProductos() }, colors = ButtonDefaults.buttonColors(containerColor = VaporPink)) {
                            Text("Reintentar")
                        }
                    }
                } else if (filteredProductos.isEmpty()) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "No se encontraron resultados para \"$searchQuery\"" else "No hay juegos disponibles.",
                        color = VaporCyanText,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredProductos) { producto ->
                            ProductCard(producto = producto) {
                                navController.navigate("productDetail/${producto.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(producto: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(BorderStroke(1.dp, VaporWhiteBorder), shape = CardDefaults.shape),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6F42C1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            AsyncImage(
                model = producto.imagenUrl,
                contentDescription = "Imagen del producto ${producto.nombre}",
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = producto.descripcion.take(80) + "...", style = MaterialTheme.typography.bodyMedium, color = VaporCyanText)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = producto.plataforma, style = MaterialTheme.typography.bodySmall, color = VaporPink)
                    Text(text = formatPrice(producto.precio), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VaporWhiteBorder)
                }
            }
        }
    }
}
