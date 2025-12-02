package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.CartViewModel
import com.example.proyectomoviles.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    navController: NavController
) {
    val productoState by remember { derivedStateOf { productViewModel.selectedProduct } }
    val isLoading by remember { derivedStateOf { productViewModel.detailsIsLoading } }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        productViewModel.fetchProductDetails(productId)
    }

    Scaffold(
        containerColor = BackgroundDark,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto", color = VaporWhiteBorder) },
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
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = VaporPink)
            } else {
                // FIX: Asignamos el estado a una variable local para el smart cast
                val producto = productoState
                if (producto != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = producto.imagenUrl,
                            contentDescription = "Imagen del producto ${producto.nombre}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = VaporWhiteBorder
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = producto.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            color = VaporCyanText
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Plataforma: ${producto.plataforma}",
                                style = MaterialTheme.typography.titleMedium,
                                color = VaporPink
                            )
                            Text(
                                text = formatPrice(producto.precio),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = VaporWhiteBorder
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Stock disponible: ${producto.stock} unidades",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (producto.stock > 0) VaporCyanText else VaporPink
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = {
                                cartViewModel.addToCart(producto)
                                scope.launch {
                                    snackbarHostState.showSnackbar("¡Juego añadido al carrito!")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = producto.stock > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VaporPink,
                                disabledContainerColor = VaporPink.copy(alpha = 0.5f)
                            )
                        ) {
                            if (producto.stock > 0) {
                                Text(text = "Añadir al Carrito")
                            } else {
                                Text(text = "Sin Stock")
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No se pudieron cargar los detalles del producto.",
                        color = VaporCyanText
                    )
                }
            }
        }
    }
}
