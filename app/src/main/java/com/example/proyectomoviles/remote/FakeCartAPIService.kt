package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.model.Producto
import kotlinx.coroutines.delay
import retrofit2.Response

class FakeCartAPIService : CartAPIService {

    private val carts = mutableMapOf<String, MutableList<CartItem>>()

    private val products = listOf(
        Producto(1, "The Witcher 3", "RPG", 29.99, "PC", "", 10),
        Producto(2, "Cyberpunk 2077", "RPG", 59.99, "PC", "", 5),
        Producto(3, "Red Dead Redemption 2", "Action", 39.99, "PS4", "", 8)
    )

    override suspend fun getCartItems(userId: String): Response<List<CartItem>> {
        delay(500) // Simulate network delay
        val userCart = carts.getOrPut(userId) { mutableListOf() }
        return Response.success(userCart)
    }

    override suspend fun addToCart(request: AddToCartRequest): Response<CartItem> {
        delay(500)
        val userCart = carts.getOrPut(request.userId) { mutableListOf() }
        val product = products.find { it.id == request.productId }
            ?: return Response.error(404, okhttp3.ResponseBody.create(null, "Product not found"))

        val existingItem = userCart.find { it.producto.id == request.productId }

        val newItem: CartItem
        if (existingItem != null) {
            val updatedQuantity = existingItem.quantity + request.quantity
            newItem = existingItem.copy(quantity = updatedQuantity)
            val index = userCart.indexOf(existingItem)
            userCart[index] = newItem
        } else {
            newItem = CartItem(producto = product, quantity = request.quantity)
            userCart.add(newItem)
        }
        return Response.success(newItem)
    }

    override suspend fun updateQuantity(request: UpdateQuantityRequest): Response<CartItem> {
        delay(500)
        val userCart = carts[request.userId] ?: return Response.error(404, okhttp3.ResponseBody.create(null, "Cart not found"))
        val existingItem = userCart.find { it.producto.id == request.productId }
            ?: return Response.error(404, okhttp3.ResponseBody.create(null, "Product not in cart"))

        val updatedItem = existingItem.copy(quantity = request.quantity)
        val index = userCart.indexOf(existingItem)
        userCart[index] = updatedItem
        return Response.success(updatedItem)
    }

    override suspend fun removeFromCart(userId: String, productId: Int): Response<Void> {
        delay(500)
        val userCart = carts[userId]
        userCart?.let {
            it.removeAll { it.producto.id == productId }
        }
        return Response.success(null)
    }

    override suspend fun checkout(userId: String): Response<Void> {
        delay(1000)
        carts.remove(userId)
        return Response.success(null)
    }
}
