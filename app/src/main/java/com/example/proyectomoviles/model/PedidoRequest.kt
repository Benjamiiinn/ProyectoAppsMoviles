package com.example.proyectomoviles.model

import com.google.gson.annotations.SerializedName

data class PedidoRequest (

    @SerializedName("idUsuario")
    val idUsuario: Int,

    // El backend espera "metodoPago"
    @SerializedName("metodoPago")
    val metodoPago: String,

    // El backend espera "items"
    @SerializedName("items")
    val items: List<ItemCompraRequest>
)

data class ItemCompraRequest(
    @SerializedName("idProducto")
    val idProducto: Int,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio")
    val precio: Int
)