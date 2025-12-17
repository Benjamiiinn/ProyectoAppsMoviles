package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Usuario
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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

// NUEVA CLASE: Para la petición de actualizar el perfil
data class UpdateProfileRequest(
    val nombre: String,
    val rut: String,
    val telefono: String?,
    val direccion: String?
)

// CORREGIDO: Se añaden anotaciones @SerializedName para que coincida con el JSON del backend
data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("role") val role: String,
    @SerializedName("user_id") val userId: Int // Es común que el backend envíe user_id en lugar de userId
)

interface AuthAPIService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun registrar(@Body request: RegisterRequest): Response<Usuario>

    @GET("usuarios/{id}")
    suspend fun getProfileById(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<Usuario>

    // NUEVO ENDPOINT: Para actualizar el perfil de usuario
    @PUT("usuarios/{id}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Path("id") userId: Int,
        @Body request: UpdateProfileRequest
    ): Response<Usuario>
}
