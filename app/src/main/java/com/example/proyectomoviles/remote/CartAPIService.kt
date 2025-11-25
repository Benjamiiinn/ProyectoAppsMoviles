package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.CartItem
import retrofit2.Response
import retrofit2.http.*

// Clases de datos para las peticiones
data class AddToCartRequest(val userId: String, val productId: Int, val quantity: Int)
data class UpdateQuantityRequest(val userId: String, val productId: Int, val quantity: Int)

interface CartAPIService {

    // Obtener el carrito de un usuario específico
    @GET("api/carrito/{userId}")
    suspend fun getCartItems(@Path("userId") userId: String): Response<List<CartItem>>

    // Añadir un item al carrito
    @POST("api/carrito/agregar")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<CartItem>

    // Actualizar la cantidad de un item
    @PUT("api/carrito/actualizar")
    suspend fun updateQuantity(@Body request: UpdateQuantityRequest): Response<CartItem>

    // Eliminar un item del carrito
    @DELETE("api/carrito/eliminar/{userId}/{productId}")
    suspend fun removeFromCart(@Path("userId") userId: String, @Path("productId") productId: Int): Response<Void>

    // Procesar el pago (checkout)
    @POST("api/pedidos/checkout")
    suspend fun checkout(@Body userId: String): Response<Void> // El servidor tomaría el carrito del usuario y crearía el pedido
}