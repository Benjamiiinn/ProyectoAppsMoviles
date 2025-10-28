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
                // Si el producto ya existe y la cantidad es menor que el stock, incrementamos
                if (existingItem.quantity < producto.stock) {
                    currentItems.map {
                        if (it.producto.id == producto.id) {
                            it.copy(quantity = it.quantity + 1)
                        } else {
                            it
                        }
                    }
                } else {
                    // Si ya se alcanzó el stock, no se hace nada
                    currentItems
                }
            } else {
                // Si es un producto nuevo y hay stock, lo añadimos
                if (producto.stock > 0) {
                    currentItems + CartItem(producto = producto)
                } else {
                    currentItems
                }
            }
        }
    }

    fun decreaseQuantity(productId: Int) {
        _cartItems.update { currentItems ->
            currentItems.mapNotNull { item ->
                if (item.producto.id == productId) {
                    if (item.quantity > 1) {
                        item.copy(quantity = item.quantity - 1)
                    } else {
                        // Si la cantidad es 1, eliminamos el producto
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
                // Solo incrementamos si la cantidad actual es menor que el stock del producto
                if (item.producto.id == productId && item.quantity < item.producto.stock) {
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