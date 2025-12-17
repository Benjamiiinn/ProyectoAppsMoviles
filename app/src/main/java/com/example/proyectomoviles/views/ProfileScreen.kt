package com.example.proyectomoviles.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectomoviles.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val currentUser = authViewModel.usuarioActual.value

    // Estados para campos editables (teléfono y dirección)
    // Convertimos teléfono a String para que sea fácil de editar en el TextField
    var telefono by remember { mutableStateOf(currentUser?.telefono?.toString() ?: "") }
    var direccion by remember { mutableStateOf(currentUser?.direccion ?: "") }

    val (messageText, isError) = authViewModel.mensaje.value

    // Actualizar los estados si el usuario cambia (ej. al cargar)
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            telefono = currentUser.telefono?.toString() ?: ""
            direccion = currentUser.direccion ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        // NOMBRE (Solo lectura)
        OutlinedTextField(
            value = currentUser?.nombre ?: "",
            onValueChange = { },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        Spacer(modifier = Modifier.height(16.dp))

        // EMAIL (Solo lectura)
        OutlinedTextField(
            value = currentUser?.email ?: "",
            onValueChange = { },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        Spacer(modifier = Modifier.height(16.dp))

        // RUT (Solo lectura)
        OutlinedTextField(
            value = currentUser?.rut ?: "",
            onValueChange = { },
            label = { Text("RUT") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        Spacer(modifier = Modifier.height(16.dp))

        // TELÉFONO (Editable)
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // DIRECCIÓN (Editable)
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (messageText.isNotEmpty()) {
            Text(
                text = messageText,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- BOTÓN CORREGIDO ---
        Button(
            onClick = {
                // Obtenemos los valores actuales de nombre y rut para enviarlos
                val currentName = currentUser?.nombre ?: ""
                val currentRut = currentUser?.rut ?: ""

                // CORRECCIÓN: Enviamos TODOS los parámetros en orden o por nombre
                authViewModel.updateUser(
                    name = currentName,
                    rut = currentRut,
                    telefono = telefono,
                    direccion = direccion
                ) { success ->
                    if (success) {
                        // El mensaje ya se maneja en el ViewModel, pero puedes forzar un refresco aquí si quieres
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text("Volver")
        }
    }
}