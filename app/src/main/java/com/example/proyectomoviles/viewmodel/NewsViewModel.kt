package com.example.proyectomoviles.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.BuildConfig
import com.example.proyectomoviles.remote.GameFromApi
import com.example.proyectomoviles.remote.RAWGApiService
import com.example.proyectomoviles.remote.RAWGRetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

class NewsViewModel : ViewModel() {

    var games by mutableStateOf<List<GameFromApi>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    private val apiService: RAWGApiService by lazy {
        RAWGRetrofitClient.instance.create(RAWGApiService::class.java)
    }

    init {
        fetchLatestGames()
    }

    fun fetchLatestGames() {
        isLoading = true
        errorMessage = ""
        viewModelScope.launch {
            try {
                // Usamos la API Key desde BuildConfig
                val response = apiService.getGames(apiKey = BuildConfig.RAWG_API_KEY)
                if (response.isSuccessful && response.body() != null) {
                    games = response.body()!!.results
                } else {
                    errorMessage = "Error al cargar las novedades: ${response.code()}"
                }
            } catch (e: IOException) {
                errorMessage = "No se pudo conectar al servidor. Revisa tu conexión."
            } catch (e: Exception) {
                errorMessage = "Ocurrió un error inesperado: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
