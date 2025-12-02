package com.example.proyectomoviles.utils

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Double): String {
    // Usamos el Builder para crear la Locale, que es el método moderno y recomendado.
    val locale = Locale.Builder().setLanguage("es").setRegion("CL").build()
    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    return currencyFormat.format(price)
}
