package com.example.proyectomoviles.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.model.ItemCompraRequest
import com.example.proyectomoviles.model.PedidoRequest
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.remote.OrderAPIService
import com.example.proyectomoviles.remote.RetrofitClient
import com.example.proyectomoviles.utils.TokenManager
import kotlinx.coroutines.launch

// Cambiamos a AndroidViewModel para tener acceso al Contexto (necesario para Retrofit)
class CartViewModel(application: Application) : AndroidViewModel(application) {

    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    // Inyectamos el servicio de pedidos
    private val orderService: OrderAPIService by lazy {
        RetrofitClient.getClient(getApplication()).create(OrderAPIService::class.java)
    }

    fun addToCart(producto: Producto) {
        // ... (Tu lógica de agregar sigue igual, la omito para ahorrar espacio) ...
        val existingItem = cartItems.find { it.producto.id == producto.id }
        if (existingItem != null) {
            if (existingItem.quantity < producto.stock) { increaseQuantity(producto.id) }
        } else {
            cartItems = cartItems + CartItem(producto = producto, quantity = 1)
        }
    }

    fun increaseQuantity(productId: Int) {
        cartItems = cartItems.map { if (it.producto.id == productId && it.quantity < it.producto.stock) it.copy(quantity = it.quantity + 1) else it }
    }

    fun decreaseQuantity(productId: Int) {
        cartItems = cartItems.mapNotNull { if (it.producto.id == productId) { val newQ = it.quantity - 1; if (newQ > 0) it.copy(quantity = newQ) else null } else it }
    }

    fun removeFromCart(productId: Int) {
        cartItems = cartItems.filterNot { it.producto.id == productId }
    }

    fun clearCart() { cartItems = emptyList() }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.producto.precio.toDouble() * it.quantity }
    }

    // --- LA CONEXIÓN REAL ---
    fun checkout(onResult: (Boolean) -> Unit) {
        if (cartItems.isEmpty()) { onResult(false); return }

        isLoading = true
        viewModelScope.launch {
            try {
                // 1. Obtener ID del usuario logueado
                val userId = TokenManager.getUserId()
                if (userId == 0) {
                    println("Error: Usuario no logueado o ID inválido")
                    onResult(false)
                    isLoading = false
                    return@launch
                }

                // 2. Transformar los items del carrito al formato que espera el Backend
                val itemsRequest = cartItems.map { cartItem ->
                    ItemCompraRequest(
                        idProducto = cartItem.producto.id,
                        cantidad = cartItem.quantity,
                        precio = cartItem.producto.precio // Precio unitario al momento de compra
                    )
                }

                // 3. Crear el Request completo
                val request = PedidoRequest(
                    idUsuario = userId,
                    metodoPago = "TARJETA", // Puedes hacerlo dinámico después
                    items = itemsRequest
                )

                // 4. Enviar a la nube
                val response = orderService.createOrder(request)

                if (response.isSuccessful) {
                    println("Compra exitosa: ${response.body()}")
                    cartItems = emptyList() // Vaciar carrito
                    onResult(true)
                } else {
                    val error = response.errorBody()?.string()
                    println("Error en compra: ${response.code()} - $error")
                    // Tip: Puedes mostrar un Toast aquí si quieres
                    Toast.makeText(getApplication(), "Error: $error", Toast.LENGTH_LONG).show()
                    onResult(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Excepción en checkout: ${e.message}")
                onResult(false)
            } finally {
                isLoading = false
            }
        }
    }
}