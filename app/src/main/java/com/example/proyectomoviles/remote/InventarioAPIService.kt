package com.example.proyectomoviles.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class InventarioResponse(
    val id: Int,
    val rawgId: Int,
    val precio: Int,
    val stock: Int
)

interface InventarioAPIService {
    @GET("api/v1/inventario")
    suspend fun obtenerInventario(): Response<InventarioResponse>
}
