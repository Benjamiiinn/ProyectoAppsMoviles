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
import com.example.proyectomoviles.remote.FakeCartAPIService
import com.example.proyectomoviles.remote.UpdateQuantityRequest
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set

    var lastSuccessfulOrder by mutableStateOf<List<CartItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var currentUserId: String? by mutableStateOf(null)
        private set

    private val apiService: CartAPIService by lazy {
        FakeCartAPIService()
    }

    fun loadCart(userId: String) {
        if (userId.isBlank()) return
        currentUserId = userId
        isLoading = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                val response = apiService.getCartItems(userId)
                if (response.isSuccessful) {
                    cartItems = response.body() ?: emptyList()
                } else {
                    errorMessage = "Error al cargar el carrito."
                }
            } catch (e: Exception) {
                errorMessage = "Ocurrió un error inesperado."
            } finally {
                isLoading = false
            }
        }
    }

    fun addToCart(producto: Producto) {
        val userId = currentUserId ?: return
        val existingItem = cartItems.find { it.producto.id == producto.id }

        if (existingItem != null) {
            increaseQuantity(producto.id)
        } else {
             viewModelScope.launch {
                try {
                    val request = AddToCartRequest(userId = userId, productId = producto.id, quantity = 1)
                    val response = apiService.addToCart(request)
                    if (response.isSuccessful) {
                        loadCart(userId)
                    } else {
                        errorMessage = "Error al añadir el producto."
                    }
                } catch (e: Exception) {
                     errorMessage = "Error inesperado al añadir el producto."
                }
            }
        }
    }

    fun increaseQuantity(productId: Int) {
        val userId = currentUserId ?: return
        val item = cartItems.find { it.producto.id == productId } ?: return
        if (item.quantity >= item.producto.stock) return

        val newQuantity = item.quantity + 1
        updateItemQuantity(userId, productId, newQuantity)
    }

    fun decreaseQuantity(productId: Int) {
        val userId = currentUserId ?: return
        val item = cartItems.find { it.producto.id == productId } ?: return

        val newQuantity = item.quantity - 1
        if (newQuantity > 0) {
            updateItemQuantity(userId, productId, newQuantity)
        } else {
            removeFromCart(productId)
        }
    }

    private fun updateItemQuantity(userId: String, productId: Int, quantity: Int) {
         viewModelScope.launch {
            try {
                val request = UpdateQuantityRequest(userId = userId, productId = productId, quantity = quantity)
                val response = apiService.updateQuantity(request)
                if (response.isSuccessful) {
                    loadCart(userId)
                } else {
                    errorMessage = "Error al actualizar la cantidad."
                }
            } catch (e: Exception) {
                errorMessage = "Error inesperado al actualizar."
            }
        }
    }

    fun removeFromCart(productId: Int) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            try {
                val response = apiService.removeFromCart(userId, productId)
                if (response.isSuccessful) {
                    loadCart(userId)
                } else {
                    errorMessage = "Error al eliminar el producto."
                }
            } catch (e: Exception) {
                errorMessage = "Error inesperado al eliminar."
            }
        }
    }

    fun checkout(onResult: (Boolean) -> Unit) {
        val userId = currentUserId ?: return
        isLoading = true
        viewModelScope.launch {
            try {
                val response = apiService.checkout(userId)
                if(response.isSuccessful) {
                    lastSuccessfulOrder = cartItems
                    cartItems = emptyList()
                    onResult(true)
                } else {
                    errorMessage = "Error durante el pago."
                    onResult(false)
                }
            } catch (e: Exception) {
                errorMessage = "Error inesperado durante el pago."
                onResult(false)
            } finally {
                isLoading = false
            }
        }
    }
}