package com.example.proyectomoviles.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Order(
    val id: Int,
    val fecha: String,
    val total: Int,
    val estado: String,
    val metodoPago: String,
    @SerializedName("detalles")
    val items: List<OrderDetail>
)

data class OrderDetail(
    val id: Int,
    val cantidad: Int,
    val precioUnitario: Int,
    val producto: Producto
)
