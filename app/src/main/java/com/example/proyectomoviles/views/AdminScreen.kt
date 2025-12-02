package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.ui.theme.*
import com.example.proyectomoviles.viewmodel.ProductViewModel
//abeja
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(productViewModel: ProductViewModel = viewModel(), navController: NavController) {
    val productos = productViewModel.productos
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf<Producto?>(null) }
    var productoToEdit by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administrador", color = VaporWhiteBorder) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = VaporPink)
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
                AdminProductCard( // Cambiado a un Card para mejor estructura
                    producto = producto,
                    onEditClick = {
                        productoToEdit = it
                        showEditDialog = true
                    },
                    onDeleteClick = { 
                        showDeleteConfirm = it
                    }
                )
                Divider(color = VaporWhiteBorder.copy(alpha = 0.2f))
            }
        }
    }

    // -- Diálogo de Edición --
    if (showEditDialog && productoToEdit != null) {
        EditProductDialog(
            producto = productoToEdit!!,
            onDismiss = { showEditDialog = false },
            onConfirm = { updatedProduct ->
                productViewModel.editProduct(updatedProduct)
                showEditDialog = false
            }
        )
    }
    
    // -- Diálogo de Confirmación de Borrado --
    if(showDeleteConfirm != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Confirmar Borrado", color = VaporPink) },
            text = { Text("¿Estás seguro de que quieres eliminar '${showDeleteConfirm!!.nombre}'?", color = VaporWhiteBorder) },
            confirmButton = {
                Button(
                    onClick = { 
                        productViewModel.deleteProduct(showDeleteConfirm!!.id)
                        showDeleteConfirm = null 
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
            containerColor = BackgroundDark,
            titleContentColor = VaporPink,
            textContentColor = VaporWhiteBorder
        )
    }
}

@Composable
fun AdminProductCard(producto: Producto, onEditClick: (Producto) -> Unit, onDeleteClick: (Producto) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
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
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = VaporGreen)
                }
                IconButton(onClick = { onDeleteClick(producto) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = VaporRed)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(producto: Producto, onDismiss: () -> Unit, onConfirm: (Producto) -> Unit) {
    var nombre by remember { mutableStateOf(producto.nombre) }
    var descripcion by remember { mutableStateOf(producto.descripcion) }
    var precio by remember { mutableStateOf(producto.precio.toString()) }
    var stock by remember { mutableStateOf(producto.stock.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Producto", color = VaporPink) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    colors = outlinedTextFieldColorsCustom()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    colors = outlinedTextFieldColorsCustom()
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    colors = outlinedTextFieldColorsCustom()
                )
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
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
                        precio = precio.toDoubleOrNull() ?: producto.precio,
                        stock = stock.toIntOrNull() ?: producto.stock
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
