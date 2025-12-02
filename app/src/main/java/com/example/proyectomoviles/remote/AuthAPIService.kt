package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Usuario
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// Clases de datos para las peticiones y respuestas
data class LoginRequest(val username: String, val password: String)

data class RegisterRequest(
    val nombre: String,
    @SerializedName("username") val email: String,
    val password: String,
    val rut: String,
    val telefono: String,
    val direccion: String
)

// Refleja la respuesta real del backend
data class AuthResponse(val token: String, val role: String, val userId: Int)

interface AuthAPIService {

    // CORREGIDO: Se elimina el prefijo "api/v1/"
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun registrar(@Body request: RegisterRequest): Response<Usuario>

    // Se asume un endpoint para obtener el perfil del usuario autenticado.
    @GET("usuarios/{id}")
    suspend fun getProfileById(
        @Header("Authorization") token: String, 
        @Path("id") userId: Int
    ): Response<Usuario>
}
