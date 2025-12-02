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
import java.io.IOException

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

    fun loadOrders(userId: String) {
        // Evita recargas innecesarias si el usuario no ha cambiado o está vacío
        if (userId.isBlank()) return

        isLoading = true
        errorMessage = ""
        viewModelScope.launch {
            try {
                val response = apiService.getOrders(userId)
                if (response.isSuccessful && response.body() != null) {
                    orders = response.body()!!
                } else {
                    errorMessage = "Error al cargar el historial de pedidos: ${response.message()}"
                    orders = emptyList()
                }
            } catch (e: IOException) {
                errorMessage = "Error de conexión al cargar los pedidos."
            } catch (e: Exception) {
                errorMessage = "Ocurrió un error inesperado al cargar los pedidos."
            } finally {
                isLoading = false
            }
        }
    }
}
