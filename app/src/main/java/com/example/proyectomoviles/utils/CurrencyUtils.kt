package com.example.proyectomoviles.utils

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Double): String {
    return try {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        format.maximumFractionDigits = 0 // No queremos decimales para CLP
        format.format(price)
    } catch (e: Exception) {
        // En caso de error, devuelve un formato simple pero claro
        "$${price.toInt()} CLP"
    }
}
