package com.example.proyectomoviles.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.model.Producto
import kotlinx.coroutines.launch

// VERSIÓN COMPLETA Y FUNCIONAL
class CartViewModel : ViewModel() {

    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set
    var lastSuccessfulOrder by mutableStateOf<List<CartItem>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var currentUserId: String? by mutableStateOf(null)
        private set

    fun loadCart(userId: String) {
        // Esta función ahora guarda el ID del usuario para usarlo después de la compra.
        // En un futuro, aquí podrías cargar un carrito guardado desde una API.
        if (userId.isNotBlank()) {
            currentUserId = userId
        }
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
                val newQuantity = it.quantity - 1
                if (newQuantity > 0) {
                    it.copy(quantity = newQuantity)
                } else {
                    null // Eliminar el item si la cantidad llega a 0
                }
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
        // CORREGIDO: Convertimos el precio a Double para la multiplicación
        return cartItems.sumOf { it.producto.precio.toDouble() * it.quantity }
    }

    fun checkout(onResult: (Boolean) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            // Aquí iría la llamada a la API real para procesar el pedido
            // Por ahora, simulamos un éxito después de un breve retardo
            kotlinx.coroutines.delay(1500)
            lastSuccessfulOrder = cartItems
            cartItems = emptyList()
            isLoading = false
            onResult(true)
        }
    }
}
