package com.example.proyectomoviles.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.gson.annotations.SerializedName


data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Int, 
    val stock: Int,
    @SerializedName("imagen")
    val imagen: String?,
    val plataforma: Plataforma?,
    val genero: Genero?
) {
    fun getBitMap(): Bitmap? {
        return try {
            if(!imagen.isNullOrEmpty()) {
                val decodedBytes = Base64.decode(imagen, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } else null
        } catch (e: Exception) {
            null
        }
    }
    }
