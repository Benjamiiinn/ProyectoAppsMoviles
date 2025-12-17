package com.example.proyectomoviles.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.Usuario
import com.example.proyectomoviles.remote.AuthAPIService
import com.example.proyectomoviles.remote.LoginRequest
import com.example.proyectomoviles.remote.RegisterRequest
import com.example.proyectomoviles.remote.RetrofitClient
import com.example.proyectomoviles.remote.UpdateProfileRequest
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
            fetchAndSetUser(TokenManager.getUserId())
        }
    }

    private fun fetchAndSetUser(userId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken() ?: return@launch
            if (userId == -1) {
                logout()
                return@launch
            }
            try {
                val response = apiService.getProfileById("Bearer $token", userId)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    TokenManager.saveAuthInfo(token, user.id, user.nombre, user.email, user.rut, user.telefono, user.direccion, TokenManager.getUserRole() ?: "USER")
                    usuarioActual.value = user
                } else {
                    Log.e("AuthViewModel", "Error al obtener el perfil, limpiando sesión.")
                    logout()
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Excepción al obtener el perfil: ${e.message}")
                logout()
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
                    TokenManager.saveAuthInfo(authResult.token, authResult.userId, "", email, "", "", "", authResult.role)
                    fetchAndSetUser(authResult.userId)
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
    
    // CORREGIDO: Se añade la función de registro que faltaba
    fun registrar(nombre: String, email: String, password: String, rut: String, telefono: String, direccion: String, onResult: (Boolean) -> Unit) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank() || rut.isBlank()) {
            mensaje.value = Pair("Nombre, email, contraseña y RUT son obligatorios.", true)
            onResult(false)
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            try {
                val request = RegisterRequest(nombre, email, password, rut, telefono, direccion)
                val response = apiService.registrar(request)

                if (response.isSuccessful) {
                    mensaje.value = Pair("¡Registro exitoso! Ya puedes iniciar sesión.", false)
                    onResult(true)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("AuthViewModel", "Error de registro: $errorBody")
                    mensaje.value = Pair("Error en el registro: $errorBody", true)
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Excepción en registro: ${e.message}")
                mensaje.value = Pair(e.message ?: "Ocurrió un error inesperado.", true)
                onResult(false)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateUser(telefono: String?, direccion: String?, onResult: (Boolean) -> Unit) {
        // ... (código existente)
    }

    fun logout() {
        TokenManager.clear()
        usuarioActual.value = null
    }

    fun isAdmin(): Boolean {
        return TokenManager.getUserRole() == "ADMIN"
    }
}
