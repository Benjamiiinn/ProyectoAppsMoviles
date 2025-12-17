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
import com.example.proyectomoviles.model.Genero
import com.example.proyectomoviles.model.Plataforma
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporCyanText
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.utils.formatPrice
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel = viewModel(),
    navController: NavController
) {
    // --- LÓGICA DE REDIRECCIÓN PARA ADMINISTRADORES RESTAURADA ---
    if (authViewModel.isAdmin()) {
        LaunchedEffect(Unit) {
            navController.navigate("admin") {
                popUpTo("home/{email}") { inclusive = true }
            }
        }
        Surface(modifier = Modifier.fillMaxSize(), color = BackgroundDark) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VaporPink)
            }
        }
        return // Detenemos la ejecución aquí para los admins
    }

    // --- UI PARA USUARIOS NORMALES ---
    val isLoading = productViewModel.isLoading
    val errorMessage = productViewModel.errorMessage

    var searchQuery by remember { mutableStateOf("") }
    val filteredProductos = productViewModel.getFilteredProducts().filter {
        it.nombre.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = { 
            TopAppBar(
                title = { 
                    Text(
                        text = "Bienvenido, ${authViewModel.usuarioActual.value?.nombre}",
                        style = MaterialTheme.typography.titleMedium,
                        color = VaporCyanText
                    )
                },
                actions = {
                    Button(onClick = { navController.navigate("novedades") }, colors = ButtonDefaults.buttonColors(containerColor = VaporPink)) {
                        Text("Novedades")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { navController.navigate("profile") }, colors = ButtonDefaults.buttonColors(containerColor = VaporCyanText)) {
                        Text("Mi Perfil", color = BackgroundDark)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { authViewModel.logout(); navController.navigate("login") { popUpTo(0) } }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("Salir")
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
                .padding(horizontal = 16.dp)
        ) {
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

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterDropdown(productViewModel.generos, productViewModel.selectedGenero, { productViewModel.onGeneroSelected(it) }, "Género", { it.nombre }, Modifier.weight(1f))
                FilterDropdown(productViewModel.plataformas, productViewModel.selectedPlataforma, { productViewModel.onPlataformaSelected(it) }, "Plataforma", { it.nombre }, Modifier.weight(1f))
                Button(onClick = { productViewModel.clearFilters() }) {
                    Text("Limpiar")
                }
            }

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
                        Button(onClick = { productViewModel.fetchInitialData() }, colors = ButtonDefaults.buttonColors(containerColor = VaporPink)) {
                            Text("Reintentar")
                        }
                    }
                } else if (filteredProductos.isEmpty()) {
                    Text(
                        text = if (searchQuery.isNotBlank() || productViewModel.selectedGenero != null || productViewModel.selectedPlataforma != null) "No se encontraron resultados." else "No hay juegos disponibles.",
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

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("DEPRECATION")
@Composable
fun <T> FilterDropdown(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T?) -> Unit,
    label: String,
    nameExtractor: (T) -> String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        OutlinedTextField(
            value = (selectedItem?.let(nameExtractor) ?: "Todos"),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            colors = outlinedTextFieldColorsCustom()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Todos") }, onClick = { 
                onItemSelected(null)
                expanded = false 
            })
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(nameExtractor(item)) },
                    onClick = { 
                        onItemSelected(item)
                        expanded = false 
                    }
                )
            }
        }
    }
}

@Composable
fun outlinedTextFieldColorsCustom() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = VaporPink,
    unfocusedBorderColor = VaporWhiteBorder.copy(alpha = 0.7f),
    focusedLabelColor = VaporPink,
    unfocusedLabelColor = VaporWhiteBorder.copy(alpha = 0.7f),
    cursorColor = VaporPink,
    focusedTextColor = VaporWhiteBorder,
    unfocusedTextColor = VaporWhiteBorder
)

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = producto.genero.nombre, style = MaterialTheme.typography.bodySmall, color = VaporCyanText)
                    Text(text = producto.plataforma.nombre, style = MaterialTheme.typography.bodySmall, color = VaporPink)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = formatPrice(producto.precio.toDouble()), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VaporWhiteBorder, modifier = Modifier.align(Alignment.End))
            }
        }
    }
}
