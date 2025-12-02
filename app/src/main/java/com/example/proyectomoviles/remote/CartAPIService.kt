package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.CartItem
import retrofit2.Response

// Clases de datos para las peticiones
data class AddToCartRequest(val userId: String, val productId: Int, val quantity: Int)
data class UpdateQuantityRequest(val userId: String, val productId: Int, val quantity: Int)

interface CartAPIService {

    // Obtener el carrito de un usuario específico
    suspend fun getCartItems(userId: String): Response<List<CartItem>>

    // Añadir un item al carrito
    suspend fun addToCart(request: AddToCartRequest): Response<CartItem>

    // Actualizar la cantidad de un item
    suspend fun updateQuantity(request: UpdateQuantityRequest): Response<CartItem>

    // Eliminar un item del carrito
    suspend fun removeFromCart(userId: String, productId: Int): Response<Void>

    // Procesar el pago (checkout)
    suspend fun checkout(userId: String): Response<Void> // El servidor tomaría el carrito del usuario y crearía el pedido
}
