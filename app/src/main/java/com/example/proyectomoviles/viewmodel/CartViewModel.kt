package com.example.proyectomoviles.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(producto: Producto) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.producto.id == producto.id }
            if (existingItem != null) {
                // Si el producto ya existe, incrementamos su cantidad
                currentItems.map {
                    if (it.producto.id == producto.id) {
                        it.copy(quantity = it.quantity + 1)
                    } else {
                        it
                    }
                }
            } else {
                // Si es un producto nuevo, lo añadimos a la lista
                currentItems + CartItem(producto = producto)
            }
        }
    }

    fun decreaseQuantity(productId: Int) {
        _cartItems.update { currentItems ->
            currentItems.mapNotNull { item ->
                if (item.producto.id == productId) {
                    if (item.quantity > 1) {
                        // Si la cantidad es mayor a 1, la reducimos
                        item.copy(quantity = item.quantity - 1)
                    } else {
                        // Si es 1, eliminamos el producto del carrito
                        null
                    }
                } else {
                    item
                }
            }
        }
    }
    
    fun increaseQuantity(productId: Int) {
        _cartItems.update { currentItems ->
            currentItems.map { item ->
                if (item.producto.id == productId) {
                    item.copy(quantity = item.quantity + 1)
                } else {
                    item
                }
            }
        }
    }

    fun removeFromCart(productId: Int) {
        _cartItems.update { currentItems ->
            currentItems.filterNot { it.producto.id == productId }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}