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

class CartViewModel(application: Application) : AndroidViewModel(application) {

    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set

    // --- AGREGADO: Variable para guardar la compra y mostrarla en ConfirmationScreen ---
    var lastSuccessfulOrder by mutableStateOf<List<CartItem>>(emptyList())
        private set

    // --- AGREGADO: Propiedad para obtener el ID del usuario actual ---
    val currentUserId: Int
        get() = TokenManager.getUserId()

    var isLoading by mutableStateOf(false)
        private set

    private val orderService: OrderAPIService by lazy {
        RetrofitClient.getClient(getApplication()).create(OrderAPIService::class.java)
    }

    fun addToCart(producto: Producto) {
        if (producto.stock <= 0) return

        val existingItem = cartItems.find { it.producto.id == producto.id }
        if (existingItem != null) {
            if (existingItem.quantity < producto.stock) {
                increaseQuantity(producto.id)
            }
        } else {
            cartItems = cartItems + CartItem(producto = producto, quantity = 1)
        }
    }

    fun increaseQuantity(productId: Int) {
        cartItems = cartItems.map {
            if (it.producto.id == productId && it.quantity < it.producto.stock) {
                it.copy(quantity = it.quantity + 1)
            } else {
                it
            }
        }
    }

    fun decreaseQuantity(productId: Int) {
        cartItems = cartItems.mapNotNull {
            if (it.producto.id == productId) {
                val newQ = it.quantity - 1
                if (newQ > 0) it.copy(quantity = newQ) else null
            } else {
                it
            }
        }
    }

    fun removeFromCart(productId: Int) {
        cartItems = cartItems.filterNot { it.producto.id == productId }
    }

    fun clearCart() {
        cartItems = emptyList()
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.producto.precio.toDouble() * it.quantity }
    }

    // --- CHECKOUT REAL CONECTADO A AWS ---
    fun checkout(onResult: (Boolean) -> Unit) {
        if (cartItems.isEmpty()) {
            onResult(false)
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val userId = TokenManager.getUserId()

                // 1. Transformar carrito al formato del Backend
                val itemsRequest = cartItems.map { cartItem ->
                    ItemCompraRequest(
                        idProducto = cartItem.producto.id,
                        cantidad = cartItem.quantity,
                        precio = cartItem.producto.precio
                    )
                }

                val request = PedidoRequest(
                    idUsuario = userId,
                    metodoPago = "TARJETA",
                    items = itemsRequest
                )

                // 2. Enviar a AWS
                val response = orderService.createOrder(request)

                if (response.isSuccessful) {
                    // 3. ¡Éxito! Guardamos copia para la pantalla de confirmación
                    lastSuccessfulOrder = ArrayList(cartItems) // Copia de seguridad

                    // 4. Limpiamos el carrito
                    cartItems = emptyList()

                    onResult(true)
                } else {
                    val error = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(getApplication(), "Error: $error", Toast.LENGTH_LONG).show()
                    onResult(false)
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_SHORT).show()
                onResult(false)
            } finally {
                isLoading = false
            }
        }
    }
}