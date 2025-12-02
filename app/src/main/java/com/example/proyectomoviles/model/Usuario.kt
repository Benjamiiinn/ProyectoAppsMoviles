package com.example.proyectomoviles.model

import com.google.gson.annotations.SerializedName

// Modelo para representar los datos de un usuario que vienen del backend.
// La contraseña se omite intencionadamente por seguridad.
data class Usuario(
    val id: Int,
    val nombre: String,
    
    // Indicamos a GSON que el campo "username" del JSON 
    // debe mapearse a nuestra propiedad "email".
    @SerializedName("username") 
    val email: String,
    
    val rut: String,
    val telefono: String?,
    val direccion: String?
)
