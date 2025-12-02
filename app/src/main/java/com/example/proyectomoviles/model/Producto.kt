package com.example.proyectomoviles.model

import com.google.gson.annotations.SerializedName

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    
    // CORREGIDO: El precio es un Int, no un Double.
    val precio: Int, 
    
    val genero: Genero,
    val plataforma: Plataforma,
    
    // CORREGIDO: La imagen puede ser nula y se mapea desde "imagen".
    @SerializedName("imagen") 
    val imagenUrl: String?,
    
    val stock: Int
)
