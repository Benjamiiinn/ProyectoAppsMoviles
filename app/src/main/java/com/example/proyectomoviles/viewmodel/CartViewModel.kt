package com.example.proyectomoviles.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.remote.AddToCartRequest
import com.example.proyectomoviles.remote.CartAPIService
import com.example.proyectomoviles.remote.RetrofitClient
import com.example.proyectomoviles.remote.UpdateQuantityRequest
import kotlinx.coroutines.launch
import java.io.IOException

class CartViewModel : ViewModel() {

    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set

    var lastSuccessfulOrder by mutableStateOf<List<CartItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    // FIX: Exponemos públicamente el ID del usuario actual de forma segura
    var currentUserId: String? by mutableStateOf(null)
        private set

    private val apiService: CartAPIService by lazy {
        RetrofitClient.instance.create(CartAPIService::class.java)
    }

    fun loadCart(userId: String) {
        if (userId.isBlank()) return
        if (userId == currentUserId && cartItems.isNotEmpty()) return

        currentUserId = userId
        isLoading = true
        errorMessage = ""

        viewModelScope.launch {
            // Simulamos la carga para el desarrollo sin backend
            try {
                // Simulación de carga (puedes añadir un delay si quieres)
                // cartItems = emptyList() // Opcional: limpiar antes de cargar
            } catch (e: Exception) {
                errorMessage = "Ocurrió un error inesperado."
            } finally {
                isLoading = false
            }
        }
    }

    fun addToCart(producto: Producto) {
        val existingItem = cartItems.find { it.producto.id == producto.id }
        if (existingItem != null) {
            increaseQuantity(producto.id)
        } else {
            cartItems = cartItems + CartItem(producto, 1)
        }
    }

    fun increaseQuantity(productId: Int) {
        cartItems = cartItems.map {
            if (it.producto.id == productId && it.quantity < it.producto.stock) {
                it.copy(quantity = it.quantity + 1)
            } else it
        }
    }

    fun decreaseQuantity(productId: Int) {
        cartItems = cartItems.mapNotNull {
            if (it.producto.id == productId) {
                if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else null
            } else {
                it
            }
        }
    }

    fun removeFromCart(productId: Int) {
        cartItems = cartItems.filterNot { it.producto.id == productId }
    }

    fun checkout(onResult: (Boolean) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            try {
                lastSuccessfulOrder = cartItems
                cartItems = emptyList()
                onResult(true)
            } catch (e: Exception) {
                errorMessage = "Error inesperado durante el pago."
                onResult(false)
            } finally {
                isLoading = false
            }
        }
    }
}