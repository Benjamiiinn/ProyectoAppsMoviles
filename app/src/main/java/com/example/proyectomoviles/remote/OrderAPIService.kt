package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Order
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz para las operaciones de la API relacionadas con los pedidos.
 */
interface OrderAPIService {

    /**
     * Obtiene el historial de pedidos para un usuario específico.
     * @param userId El ID del usuario cuyos pedidos se quieren obtener.
     * @return Una lista de objetos [Order].
     */
    @GET("api/orders/{userId}")
    suspend fun getOrders(@Path("userId") userId: String): Response<List<Order>>
}
