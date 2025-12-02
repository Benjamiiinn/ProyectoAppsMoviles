package com.example.proyectomoviles.remote

import com.example.proyectomoviles.utils.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Esta es la IP especial para que el emulador se conecte al localhost de tu PC
    private const val BASE_URL = "http://127.0.0.1:8080/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val token = TokenManager.getToken()

            val newRequest = if (token != null) {
                // Si hay token, lo agregamos al header
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }
            chain.proceed(newRequest)
        }
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}