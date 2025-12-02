package com.example.proyectomoviles.model

import java.util.Date

/**
 * Representa un pedido realizado por un usuario.
 *
 * @param id El identificador único del pedido.
 * @param date La fecha en que se realizó el pedido.
 * @param items La lista de productos y cantidades que componen el pedido.
 * @param total El coste total del pedido.
 */
data class Order(
    val id: String,
    val date: Date,
    val items: List<CartItem>,
    val total: Double
)
