package com.example.proyectomoviles.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Esta es la IP especial para que el emulador se conecte al localhost de tu PC
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}