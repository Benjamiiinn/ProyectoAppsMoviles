package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Order
import com.example.proyectomoviles.model.PedidoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderAPIService {

    // Asumimos un endpoint como este para obtener los pedidos de un usuario
    @GET("api/v1/pedidos/mis-pedidos")
    suspend fun getMyOrders(): Response<List<Order>>

    @POST("api/v1/pedidos/checkout")
    suspend fun createOrder(@Body orderRequest: PedidoRequest): Response<Order>
}
