package com.example.proyectomoviles.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.Order
import com.example.proyectomoviles.remote.OrderAPIService
import com.example.proyectomoviles.remote.RetrofitClient
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    var orders by mutableStateOf<List<Order>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    private val apiService: OrderAPIService by lazy {
        RetrofitClient.instance.create(OrderAPIService::class.java)
    }

    fun fetchOrders(userId: String) {
        isLoading = true
        errorMessage = ""
        viewModelScope.launch {
            try {
                val response = apiService.getOrders(userId)
                if (response.isSuccessful && response.body() != null) {
                    orders = response.body()!!
                } else {
                    errorMessage = "Error al cargar los pedidos."
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
