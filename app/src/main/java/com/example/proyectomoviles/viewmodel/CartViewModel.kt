package com.example.proyectomoviles.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.CartItem
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.remote.CartAPIService
import com.example.proyectomoviles.remote.RetrofitClient
import kotlinx.coroutines.launch

class CartViewModel(private val productViewModel: ProductViewModel) : ViewModel() {

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
        RetrofitClient.instance.create(CartAPIService::class.java)
    }

    fun loadCart(userId: String) {
        if (userId.isBlank()) return
        currentUserId = userId
    }

    fun addToCart(producto: Producto) {
        val existingItem = cartItems.find { it.producto.id == producto.id }
        if (existingItem != null) {
            increaseQuantity(producto.id)
        } else {
            if (productViewModel.decreaseStock(producto.id, 1)) {
                val updatedProduct = productViewModel.productos.find { it.id == producto.id }
                updatedProduct?.let { cartItems = cartItems + CartItem(it, 1) }
            }
        }
    }

    fun increaseQuantity(productId: Int) {
        val item = cartItems.find { it.producto.id == productId }
        if (item != null && productViewModel.decreaseStock(productId, 1)) {
            val updatedProduct = productViewModel.productos.find { it.id == productId }
            updatedProduct?.let {
                cartItems = cartItems.map {
                    if (it.producto.id == productId) {
                        it.copy(quantity = it.quantity + 1, producto = updatedProduct)
                    } else it
                }
            }
        }
    }

    fun decreaseQuantity(productId: Int) {
        val item = cartItems.find { it.producto.id == productId }
        if (item != null) {
            productViewModel.increaseStock(productId, 1)
            val updatedProduct = productViewModel.productos.find { it.id == productId }
            updatedProduct?.let {
                cartItems = cartItems.mapNotNull {
                    if (it.producto.id == productId) {
                        if (it.quantity > 1) it.copy(quantity = it.quantity - 1, producto = updatedProduct) else null
                    } else {
                        it
                    }
                }
            }
        }
    }


    fun removeFromCart(productId: Int) {
        val item = cartItems.find { it.producto.id == productId }
        if (item != null) {
            productViewModel.increaseStock(productId, item.quantity)
            cartItems = cartItems.filterNot { it.producto.id == productId }
        }
    }

    fun checkout(onResult: (Boolean) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            try {
                // Lógica de pago...
                lastSuccessfulOrder = cartItems
                cartItems = emptyList()
                onResult(true)
            } catch (e: Exception) {
                errorMessage = "Error inesperado durante el pago."
                // Devolver el stock si el pago falla
                lastSuccessfulOrder.forEach { item ->
                    productViewModel.increaseStock(item.producto.id, item.quantity)
                }
                onResult(false)
            } finally {
                isLoading = false
            }
        }
    }
}
