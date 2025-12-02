package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Genero
import com.example.proyectomoviles.model.Plataforma
import com.example.proyectomoviles.model.Producto
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// --- Data Classes para las peticiones ---
data class CreateProductRequest(
    val nombre: String,
    val descripcion: String,
    val precio: Int, // CORREGIDO: Cambiado a Int
    val stock: Int,
    @SerializedName("imagen") val imagenUrl: String?,
    @SerializedName("plataforma_id") val plataformaId: Int,
    @SerializedName("genero_id") val generoId: Int
)

// CORREGIDO: El precio ahora es un Int
data class UpdateProductRequest(
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int
)

interface ProductAPIService {

    @GET("api/v1/productos")
    suspend fun getProducts(): Response<List<Producto>>

    @GET("api/v1/generos")
    suspend fun getGeneros(): Response<List<Genero>>

    @GET("api/v1/plataformas")
    suspend fun getPlataformas(): Response<List<Plataforma>>

    @POST("api/v1/productos")
    suspend fun createProduct(@Body product: CreateProductRequest): Response<Producto>

    @PUT("api/v1/productos/{id}")
    suspend fun updateProduct(@Path("id") productId: Int, @Body product: UpdateProductRequest): Response<Producto>

    @DELETE("api/v1/productos/{id}")
    suspend fun deleteProduct(@Path("id") productId: Int): Response<Void>
}
