package com.example.proyectomoviles.viewmodel

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.Usuario
import com.example.proyectomoviles.remote.AuthAPIService
import com.example.proyectomoviles.remote.LoginRequest
import com.example.proyectomoviles.remote.RegisterRequest
import com.example.proyectomoviles.remote.RetrofitClient
import com.example.proyectomoviles.utils.TokenManager
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var mensaje = mutableStateOf<Pair<String, Boolean>>(Pair("", false))
    var usuarioActual = mutableStateOf<Usuario?>(null)
    var isLoading = mutableStateOf(false)

    private val apiService: AuthAPIService by lazy {
        RetrofitClient.instance.create(AuthAPIService::class.java)
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
        // ... (lógica de registro)
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
