package com.example.proyectomoviles.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.Order
import com.example.proyectomoviles.remote.OrderAPIService
import com.example.proyectomoviles.remote.RetrofitClient
import kotlinx.coroutines.launch

class OrdersViewModel(application: Application) : AndroidViewModel(application) {

    var orders by mutableStateOf<List<Order>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    private val apiService: OrderAPIService by lazy {
        RetrofitClient.getClient(getApplication()).create(OrderAPIService::class.java)
    }

    // Iniciamos la carga apenas se crea el ViewModel
    init {
        fetchOrders()
    }

    fun fetchOrders() {
        isLoading = true
        errorMessage = ""
        viewModelScope.launch {
            try {
                // El backend sabe quién eres por el Token, no hace falta enviar ID
                val response = apiService.getMyOrders()

                if (response.isSuccessful && response.body() != null) {
                    orders = response.body()!!
                } else {
                    errorMessage = "No se pudieron cargar los pedidos: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}