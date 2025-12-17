package com.example.proyectomoviles.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectomoviles.model.Genero
import com.example.proyectomoviles.model.Plataforma
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.ui.theme.BackgroundDark
import com.example.proyectomoviles.ui.theme.VaporPink
import com.example.proyectomoviles.ui.theme.VaporWhiteBorder
import com.example.proyectomoviles.viewmodel.ProductViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController, productViewModel: ProductViewModel = viewModel()) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val file = remember { context.createImageFile() }
    val uri = remember(file) { FileProvider.getUriForFile(Objects.requireNonNull(context), context.packageName + ".provider", file) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { imageUri = uri }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> imageUri = uri }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) { cameraLauncher.launch(uri) } 
        else { Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show() }
    }

    var selectedGenero by remember { mutableStateOf<Genero?>(null) }
    var selectedPlataforma by remember { mutableStateOf<Plataforma?>(null) }

    val generos = productViewModel.generos
    val plataformas = productViewModel.plataformas

    val isFormValid by derivedStateOf {
        nombre.isNotBlank() && precio.isNotBlank() && stock.isNotBlank() &&
        descripcion.isNotBlank() && imageUri != null &&
        selectedGenero != null && selectedPlataforma != null
    }

    val customTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = VaporPink,
        unfocusedBorderColor = VaporWhiteBorder,
        focusedLabelColor = VaporPink,
        unfocusedLabelColor = VaporWhiteBorder,
        cursorColor = Color(0xFF32FBE2), // VaporCyanText
        focusedTextColor = VaporWhiteBorder,
        unfocusedTextColor = VaporWhiteBorder
    )

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto", color = VaporWhiteBorder) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = VaporPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            // --- ZONA DE SCROLL PARA EL FORMULARIO ---
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp).border(BorderStroke(1.dp, VaporWhiteBorder)), contentAlignment = Alignment.Center) {
                    if (imageUri != null) {
                        AsyncImage(model = imageUri, contentDescription = "Imagen seleccionada", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Text("Selecciona una imagen", color = VaporWhiteBorder)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { galleryLauncher.launch("image/*") }, modifier = Modifier.weight(1f)) { Text("Galería") }
                    Button(onClick = {
                        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }, modifier = Modifier.weight(1f)) { Text("Cámara") }
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                // --- CAMPOS RESTAURADOS ---
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del Producto") }, modifier = Modifier.fillMaxWidth(), colors = customTextFieldColors)
                Spacer(modifier = Modifier.height(8.dp))
                FilterDropdown(generos, selectedGenero, { selectedGenero = it }, "Género", { it.nombre }, Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                FilterDropdown(plataformas, selectedPlataforma, { selectedPlataforma = it }, "Plataforma", { it.nombre }, Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = precio, onValueChange = { precio = it.filter { c -> c.isDigit() } }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = customTextFieldColors)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = stock, onValueChange = { stock = it.filter { c -> c.isDigit() } }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = customTextFieldColors)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(120.dp), colors = customTextFieldColors)
            }

            // --- ZONA FIJA PARA EL BOTÓN DE GUARDAR ---
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    isSaving = true
                    val newProduct = Producto(
                        id = 0, 
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio.toIntOrNull() ?: 0, 
                        stock = stock.toIntOrNull() ?: 0,
                        genero = selectedGenero!!,
                        plataforma = selectedPlataforma!!,
                        imagenUrl = imageUri.toString()
                    )
                    productViewModel.addProduct(newProduct) { success ->
                        isSaving = false
                        if (success) {
                            Toast.makeText(context, "Producto añadido con éxito", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Error al añadir el producto", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = isFormValid && !isSaving, 
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(30.dp), color = Color.White)
                } else {
                    Text("Confirmar y Guardar Producto")
                }
            }
        }
    }
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(imageFileName, ".jpg", externalCacheDir)
}

