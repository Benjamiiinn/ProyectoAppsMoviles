package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Usuario
import retrofit2.Response

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val nombre: String, val email: String, val password: String, val rut: String)

interface UserAPIService {
    suspend fun login(request: LoginRequest): Response<Usuario>
    suspend fun register(request: RegisterRequest): Response<Usuario>
}
