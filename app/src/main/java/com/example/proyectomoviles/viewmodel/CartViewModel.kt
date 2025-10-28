package com.example.proyectomoviles.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectomoviles.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<Producto>>(emptyList())
    val cartItems: StateFlow<List<Producto>> = _cartItems.asStateFlow()

    fun addToCart(producto: Producto) {
        _cartItems.update { currentItems ->
            // Evita añadir el mismo producto varias veces
            if (currentItems.find { it.id == producto.id } == null) {
                currentItems + producto
            } else {
                currentItems
            }
        }
    }

    fun removeFromCart(producto: Producto) {
        _cartItems.update { currentItems ->
            currentItems.filterNot { it.id == producto.id }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
