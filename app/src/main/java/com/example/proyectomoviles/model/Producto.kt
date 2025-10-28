package com.example.proyectomoviles.model

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val plataforma: String,
    val imagenUrl: String,
    val stock: Int
)
