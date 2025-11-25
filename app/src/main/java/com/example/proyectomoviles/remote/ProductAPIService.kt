package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Producto
import retrofit2.Response
import retrofit2.http.GET

interface ProductAPIService {

    @GET("api/productos") // Asumimos que este es el endpoint en tu servidor Spring Boot
    suspend fun getProductos(): Response<List<Producto>>

    // Aquí podrías añadir otros endpoints, como:
    // @GET("api/productos/{id}")
    // suspend fun getProductoById(@Path("id") id: Int): Response<Producto>
    //
    // @POST("api/productos")
    // suspend fun addProducto(@Body producto: Producto): Response<Producto>
}
