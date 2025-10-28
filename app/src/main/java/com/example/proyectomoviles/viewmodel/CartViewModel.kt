package com.example.proyectomoviles.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.model.FakeProductService
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
                if (existingItem.quantity < producto.stock) {
                    currentItems.map { if (it.producto.id == producto.id) it.copy(quantity = it.quantity + 1) else it }
                } else { currentItems }
            } else {
                if (producto.stock > 0) { currentItems + CartItem(producto = producto) } else { currentItems }
            }
        }
    }

    fun decreaseQuantity(productId: Int) {
        _cartItems.update { currentItems ->
            currentItems.mapNotNull { item ->
                if (item.producto.id == productId) {
                    if (item.quantity > 1) item.copy(quantity = item.quantity - 1) else null
                } else item
            }
        }
    }
    
    fun increaseQuantity(productId: Int) {
        _cartItems.update { currentItems ->
            currentItems.map {
                if (it.producto.id == productId && it.quantity < it.producto.stock) {
                    it.copy(quantity = it.quantity + 1)
                } else it
            }
        }
    }

    fun removeFromCart(productId: Int) {
        _cartItems.update { currentItems -> currentItems.filterNot { it.producto.id == productId } }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    /**
     * Simula el proceso de pago, validando el stock de cada producto.
     * @return `true` si la compra es exitosa, `false` si hay un error de stock.
     */
    fun checkout(): Boolean {
        // 1. Obtener la lista de productos real para comprobar el stock más actualizado
        val productosReales = FakeProductService.productos

        // 2. Validar que cada producto en el carrito tenga stock suficiente
        for (cartItem in _cartItems.value) {
            val productoReal = productosReales.find { it.id == cartItem.producto.id }

            // Si el producto no existe o el stock es insuficiente, la compra falla
            if (productoReal == null || productoReal.stock < cartItem.quantity) {
                return false // Error de stock
            }
        }

        // 3. Si todo está en orden, descontar el stock y proceder
        for (cartItem in _cartItems.value) {
            val productoReal = productosReales.find { it.id == cartItem.producto.id }!!
            FakeProductService.updateStock(cartItem.producto.id, productoReal.stock - cartItem.quantity)
        }

        // 4. Limpiar el carrito y devolver éxito
        clearCart()
        return true
    }
}