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

    // Estados para cada campo editable
    var telefono by remember { mutableStateOf(currentUser?.telefono ?: "") }
    var direccion by remember { mutableStateOf(currentUser?.direccion ?: "") }
    
    val (messageText, isError) = authViewModel.mensaje.value

    // Actualizar los estados si el usuario cambia (por ejemplo, al cargar por primera vez)
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            telefono = currentUser.telefono ?: ""
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

        OutlinedTextField(
            value = currentUser?.nombre ?: "",
            onValueChange = { /* No se hace nada */ },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // CORREGIDO: No se permite editar el nombre
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = currentUser?.email ?: "",
            onValueChange = { /* No se hace nada */ },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // No se permite editar el email
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = currentUser?.rut ?: "",
            onValueChange = { /* No se hace nada */ },
            label = { Text("RUT") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // CORREGIDO: No se permite editar el RUT
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

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

        Button(
            onClick = {
                // Llamada actualizada para enviar solo los campos editables
                authViewModel.updateUser(currentUser?.nombre ?: "", currentUser?.rut ?: "", telefono, direccion) { success ->
                    if (success) {
                        authViewModel.mensaje.value = Pair("¡Datos actualizados con éxito!", false)
                    } else {
                         authViewModel.mensaje.value = Pair("No se pudieron actualizar los datos.", true)
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
