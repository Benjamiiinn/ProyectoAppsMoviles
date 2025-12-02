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

    // --- ESTADOS DE LA UI ---
    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set

    // Nuevo estado para guardar el último pedido exitoso
    var lastSuccessfulOrder by mutableStateOf<List<CartItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    private var currentUserId: String? = null

    private val apiService: CartAPIService by lazy {
        RetrofitClient.instance.create(CartAPIService::class.java)
    }

    // --- FUNCIONES PÚBLICAS ---

    fun loadCart(userId: String) {
        if (userId.isBlank() || userId == currentUserId) return

        currentUserId = userId
        isLoading = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                val response = apiService.getCartItems(userId)
                if (response.isSuccessful && response.body() != null) {
                    cartItems = response.body()!!
                } else {
                    errorMessage = "Error al cargar el carrito: ${response.message()}"
                    cartItems = emptyList()
                }
            } catch (e: IOException) {
                errorMessage = "Error de conexión al cargar el carrito."
            } catch (e: Exception) {
                errorMessage = "Ocurrió un error inesperado."
            } finally {
                isLoading = false
            }
        }
    }

    fun addToCart(producto: Producto) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            val request = AddToCartRequest(userId = userId, productId = producto.id, quantity = 1)
            try {
                val response = apiService.addToCart(request)
                if (response.isSuccessful) {
                    loadCart(userId)
                }
            } catch (e: Exception) { /* Manejar error */ }
        }
    }

    fun increaseQuantity(productId: Int) {
        val item = cartItems.find { it.producto.id == productId } ?: return
        updateQuantity(productId, item.quantity + 1)
    }

    fun decreaseQuantity(productId: Int) {
        val item = cartItems.find { it.producto.id == productId } ?: return
        val newQuantity = item.quantity - 1
        if (newQuantity > 0) {
            updateQuantity(productId, newQuantity)
        } else {
            removeFromCart(productId)
        }
    }

    fun removeFromCart(productId: Int) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            try {
                val response = apiService.removeFromCart(userId, productId)
                if (response.isSuccessful) {
                    loadCart(userId)
                }
            } catch (e: Exception) { /* Manejar error */ }
        }
    }

    fun checkout(onResult: (Boolean) -> Unit) {
        val userId = currentUserId ?: run {
            onResult(false)
            return
        }
        isLoading = true
        viewModelScope.launch {
            try {
                // Simulamos una llamada exitosa ya que no hay backend
                // val response = apiService.checkout(userId)
                // if (response.isSuccessful) {
                    // Guardamos el pedido actual antes de limpiar el carrito
                    lastSuccessfulOrder = cartItems
                    cartItems = emptyList() 
                    onResult(true)
                // } else {
                //     errorMessage = "El pago falló: ${response.message()}"
                //     onResult(false)
                // }
            } catch (e: Exception) {
                errorMessage = "Error de conexión durante el pago."
                onResult(false)
            } finally {
                isLoading = false
            }
        }
    }
    
    private fun updateQuantity(productId: Int, quantity: Int) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            val request = UpdateQuantityRequest(userId, productId, quantity)
            try {
                val response = apiService.updateQuantity(request)
                if (response.isSuccessful) {
                    loadCart(userId)
                }
            } catch (e: Exception) { /* Manejar error */ }
        }
    }
}
