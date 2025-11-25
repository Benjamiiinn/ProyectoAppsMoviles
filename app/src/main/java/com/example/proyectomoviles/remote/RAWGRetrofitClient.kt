package com.example.proyectomoviles.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RAWGRetrofitClient {
    // URL base de la API de RAWG
    private const val BASE_URL = "https://api.rawg.io/api/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
