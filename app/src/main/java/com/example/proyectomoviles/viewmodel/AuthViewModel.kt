package com.example.proyectomoviles.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.Usuario
import com.example.proyectomoviles.remote.AuthAPIService
import com.example.proyectomoviles.remote.LoginRequest
import com.example.proyectomoviles.remote.RegisterRequest
import com.example.proyectomoviles.remote.RetrofitClient
import com.example.proyectomoviles.utils.TokenManager
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    var mensaje = mutableStateOf<Pair<String, Boolean>>(Pair("", false))
    var usuarioActual = mutableStateOf<Usuario?>(null)
    var isLoading = mutableStateOf(false)

    private val apiService: AuthAPIService by lazy {
        RetrofitClient.getClient(getApplication()).create(AuthAPIService::class.java)
    }

    init {
        if (TokenManager.isLoggedIn()) {
            loadUserProfile()
        }
    }

    fun loadUserProfile() {
        usuarioActual.value = Usuario(
            id = TokenManager.getUserId(),
            nombre = TokenManager.getUserName() ?: "",
            email = TokenManager.getUserEmail() ?: "",
            rut = TokenManager.getUserRut() ?: "",
            telefono = TokenManager.getUserTelefono(),
            direccion = TokenManager.getUserDireccion()
        )
    }

    fun isAdmin(): Boolean {
        return TokenManager.getUserRole() == "ADMIN"
    }

    fun updateUser(name: String, rut: String, telefono: String?, direccion: String?, onResult: (Boolean) -> Unit) {
        val currentToken = TokenManager.getToken() ?: return
        val currentUserId = TokenManager.getUserId()
        val currentEmail = TokenManager.getUserEmail() ?: return

        // TODO: Implementar llamada a la API del backend para actualizar el perfil.
        TokenManager.saveAuthInfo(currentToken, currentUserId, name, currentEmail, rut, telefono, direccion, TokenManager.getUserRole() ?: "USER")
        loadUserProfile()
        onResult(true)
    }

    fun registrar(nombre: String, email: String, password: String, rut: String, telefono: String, direccion: String, onResult: (Boolean) -> Unit) {
        isLoading.value = true
        mensaje.value = Pair("", false)

        if (nombre.isBlank() || email.isBlank() || password.isBlank() || rut.isBlank()) {
            mensaje.value = Pair("Por favor completa los campos obligatorios", true)
            isLoading.value = false
            onResult(false)
            return
        }

        val telefonoInt = try {
            if (telefono.isBlank()) 0 else telefono.toInt()
        } catch (e: NumberFormatException) {
            mensaje.value = Pair("El teléfono debe ser un número válido", true)
            isLoading.value = false
            onResult(false)
            return
        }

        viewModelScope.launch {
            try {
                // 4. Crear el objeto con el INT correcto
                val request = RegisterRequest(
                    nombre = nombre,
                    email = email,
                    password = password,
                    rut = rut,
                    telefono = telefonoInt,
                    direccion = direccion
                )

                // 5. Llamada a la API
                val response = apiService.registrar(request)

                if (response.isSuccessful && response.body() != null) {
                    val authResult = response.body()!!

                    // Opcional: Autologuear al usuario recién registrado
                    TokenManager.saveAuthInfo(
                        token = authResult.token,
                        userId = authResult.userId,
                        name = nombre,
                        email = email,
                        rut = rut,
                        telefono = telefono,
                        direccion = direccion,
                        role = authResult.role
                    )
                    loadUserProfile() // Actualizar estado en la app

                    mensaje.value = Pair("Registro exitoso", false)
                    onResult(true)
                } else {
                    // Leer el error que devuelve el backend (ej. "El email ya existe")
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    mensaje.value = Pair("Error: $errorBody", true)
                    onResult(false)
                }
            } catch (e: Exception) {
                mensaje.value = Pair("Error de conexión: ${e.message}", true)
                onResult(false)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        isLoading.value = true
        mensaje.value = Pair("", false)
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(username = email, password = password)
                val response = apiService.login(loginRequest)

                if (response.isSuccessful && response.body() != null) {
                    val authResult = response.body()!!
                    
                    // Como el login no devuelve los datos del usuario, los guardamos parcialmente.
                    TokenManager.saveAuthInfo(
                        token = authResult.token,
                        userId = authResult.userId,
                        name = email, // Usamos el email como nombre temporal
                        email = email, 
                        rut = "", // No tenemos el RUT
                        telefono = "", // No tenemos el teléfono
                        direccion = "", // No tenemos la dirección
                        role = authResult.role
                    )
                    
                    loadUserProfile()
                    mensaje.value = Pair("Inicio de sesión exitoso", false)
                    onResult(true)
                } else {
                    mensaje.value = Pair("Credenciales inválidas", true)
                    onResult(false)
                }
            } catch (e: Exception) {
                mensaje.value = Pair(e.message ?: "Ocurrió un error inesperado.", true)
                onResult(false)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun logout() {
        TokenManager.clear()
        usuarioActual.value = null
    }
    
    // ... (lógica de validación)
}
