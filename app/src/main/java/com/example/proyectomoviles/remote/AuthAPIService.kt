package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Clases de datos para las peticiones y respuestas
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val nombre: String, val email: String, val password: String, val rut: String)
// Podrías tener una clase específica para la respuesta si el servidor no devuelve el objeto Usuario completo
data class AuthResponse(val token: String, val user: Usuario) // Ejemplo de una posible respuesta

interface AuthAPIService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun registrar(@Body request: RegisterRequest): Response<Void> // Usamos Void si el servidor solo responde con éxito/error y no un cuerpo

}