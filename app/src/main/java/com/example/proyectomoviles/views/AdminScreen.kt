package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.ui.theme.*
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    authViewModel: AuthViewModel, 
    productViewModel: ProductViewModel = viewModel(), 
    navController: NavController
) {
    val productos = productViewModel.productos
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf<Producto?>(null) }
    var productoToEdit by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administrador", color = VaporWhiteBorder) },
                actions = {
                    Button(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate("login") { popUpTo(0) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Salir")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addProduct") },
                containerColor = VaporPink
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto", tint = Color.White)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Gestión de Productos",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = VaporWhiteBorder
                )
            }
            items(productos) { producto ->
                AdminProductCard(
                    producto = producto,
                    onEditClick = {
                        productoToEdit = it
                        showEditDialog = true
                    },
                    onDeleteClick = { 
                        showDeleteConfirm = it
                    }
                )
                HorizontalDivider(color = VaporWhiteBorder.copy(alpha = 0.2f))
            }
        }
    }

    if (showEditDialog && productoToEdit != null) {
        EditProductDialog(
            producto = productoToEdit!!,
            productViewModel = productViewModel,
            onDismiss = { showEditDialog = false },
            onConfirm = { updatedProduct ->
                productViewModel.editProduct(updatedProduct) { success ->
                    if (success) {
                        showEditDialog = false
                    }
                }
            }
        )
    }
    
    if(showDeleteConfirm != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Confirmar Borrado", color = VaporPink) },
            text = { Text("¿Estás seguro de que quieres eliminar '${showDeleteConfirm!!.nombre}'?", color = VaporWhiteBorder) },
            confirmButton = {
                Button(
                    onClick = { 
                        productViewModel.deleteProduct(showDeleteConfirm!!.id) { success ->
                            if (success) {
                                showDeleteConfirm = null
                            }
                        } 
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VaporPink)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) {
                    Text("Cancelar", color = VaporCyanText)
                }
            },
            containerColor = BackgroundDark
        )
    }
}

@Composable
fun AdminProductCard(producto: Producto, onEditClick: (Producto) -> Unit, onDeleteClick: (Producto) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            producto.nombre,
            modifier = Modifier.weight(1f),
            color = VaporCyanText,
            style = MaterialTheme.typography.bodyLarge
        )
        Row {
            IconButton(onClick = { onEditClick(producto) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Green)
            }
            IconButton(onClick = { onDeleteClick(producto) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(
    producto: Producto, 
    productViewModel: ProductViewModel,
    onDismiss: () -> Unit, 
    onConfirm: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(producto.nombre) }
    var descripcion by remember { mutableStateOf(producto.descripcion) }
    var precio by remember { mutableStateOf(producto.precio.toString()) }
    var stock by remember { mutableStateOf(producto.stock.toString()) }
    var selectedGenero by remember { mutableStateOf(producto.genero) }
    var selectedPlataforma by remember { mutableStateOf(producto.plataforma) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Producto", color = VaporPink) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, colors = outlinedTextFieldColorsCustom())
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, colors = outlinedTextFieldColorsCustom())
                
                FilterDropdown(productViewModel.generos, selectedGenero, { selectedGenero = it!! }, "Género", { it.nombre })
                FilterDropdown(productViewModel.plataformas, selectedPlataforma, { selectedPlataforma = it!! }, "Plataforma", { it.nombre })

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it.filter { c -> c.isDigit() } },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = outlinedTextFieldColorsCustom()
                )
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it.filter { c -> c.isDigit() } },
                    label = { Text("Stock") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = outlinedTextFieldColorsCustom()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    val updatedProduct = producto.copy(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio.toIntOrNull() ?: producto.precio,
                        stock = stock.toIntOrNull() ?: producto.stock,
                        genero = selectedGenero,
                        plataforma = selectedPlataforma
                    )
                    onConfirm(updatedProduct)
                },
                colors = ButtonDefaults.buttonColors(containerColor = VaporPink)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = VaporCyanText)
            }
        },
        containerColor = BackgroundDark
    )
}

