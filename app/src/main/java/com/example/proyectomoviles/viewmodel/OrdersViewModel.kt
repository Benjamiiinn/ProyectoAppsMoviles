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
import com.example.proyectomoviles.utils.TokenManager // Importante
import kotlinx.coroutines.launch

class OrdersViewModel(application: Application) : AndroidViewModel(application) {

    var orders by mutableStateOf<List<Order>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    // --- AGREGADO: Propiedades que faltaban para que OrdersScreen no de error ---
    val currentUserId: Int
        get() = TokenManager.getUserId()

    val currentUserEmail: String
        get() = TokenManager.getUserEmail() ?: ""

    private val apiService: OrderAPIService by lazy {
        RetrofitClient.getClient(getApplication()).create(OrderAPIService::class.java)
    }

    init {
        fetchOrders()
    }

    fun fetchOrders() {
        isLoading = true
        errorMessage = ""
        viewModelScope.launch {
            try {
                // El backend identifica al usuario por el Token en la cabecera
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