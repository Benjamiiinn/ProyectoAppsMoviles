package com.example.proyectomoviles.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Clases de datos para las peticiones y respuestas
data class LoginRequest(val username: String, val password: String)

data class RegisterRequest(
    val nombre: String,
    @SerializedName("username") val email: String,
    val password: String,
    val rut: String,
    val telefono: Int,
    val direccion: String
)

// Refleja la respuesta real del backend
data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("role") val role: String,
    @SerializedName("userId") val userId: Int
)

interface AuthAPIService {

    // CORREGIDO: Se elimina el prefijo "api/v1/"
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun registrar(@Body request: RegisterRequest): Response<AuthResponse>
}
