package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Order
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderAPIService {

    // Asumimos un endpoint como este para obtener los pedidos de un usuario
    @GET("orders/{userId}")
    suspend fun getOrders(@Path("userId") userId: String): Response<List<Order>>
}
