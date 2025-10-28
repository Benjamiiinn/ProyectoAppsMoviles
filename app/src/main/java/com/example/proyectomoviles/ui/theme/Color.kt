package com.example.proyectomoviles.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val BackgroundDark = Color(0xFF1A0933)
val SurfaceDark = Color(0xFF16213E)

val VaporInputBackground = Color(0xFF30115E)

val VaporCyanText = Color(0xFF32FBE2)      // Color que quieres para el texto dentro

val VaporPurpleBorder = Color(0xFF6F42C1) // Color para el borde

val VaporMutedText = Color(0xFFAAAAAA)

val VaporPink = Color(0xFFEA39B8)

val VaporError = Color(0xFFE44C55)

val VaporWhiteBorder = Color(0xFFDEE2E6)

@Composable
fun outlinedTextFieldColorsCustom(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        // --- Colores del Texto ---
        focusedTextColor = VaporCyanText,     // Color del texto al escribir (enfocado)
        unfocusedTextColor = VaporCyanText,   // Color del texto al escribir (sin foco)
        // --- Colores del Contenedor (Fondo de la casilla) ---
        focusedContainerColor = VaporInputBackground, // Fondo cuando está enfocado
        unfocusedContainerColor = VaporInputBackground, // Fondo cuando no está enfocado
        disabledContainerColor = VaporInputBackground.copy(alpha = 0.5f), // Fondo deshabilitado
        // --- Colores del Borde ---
        focusedBorderColor = VaporPink,       // Borde cuando está enfocado (rosa)
        unfocusedBorderColor = VaporPurpleBorder, // Borde cuando no está enfocado (morado)
        // --- Colores del Label (Etiqueta flotante) ---
        focusedLabelColor = VaporPink,        // Color del label cuando está enfocado (rosa)
        unfocusedLabelColor = VaporMutedText, // Color del label cuando no está enfocado (gris)
        // --- Otros colores ---
        cursorColor = VaporPink,              // Color del cursor
        // Puedes definir colores para error también
        errorBorderColor = VaporError,
        errorLabelColor = VaporError,
        errorTextColor = VaporCyanText // Mantener texto cian aunque haya error
    )
}

