package com.example.proyectomoviles.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// --- Data Classes para la respuesta de la API de RAWG ---

// El objeto principal que devuelve la API
data class GamesResponse(
    val results: List<GameFromApi>
)

// Representa un solo juego de la API
data class GameFromApi(
    val id: Int,
    val name: String,
    val background_image: String? // La URL de la imagen puede ser nula
)

// --- Interfaz de Retrofit para la API de RAWG ---

interface RAWGApiService {

    /**
     * Obtiene una lista de juegos.
     * @param apiKey Tu clave de API personal de RAWG.
     * @param page El número de página de resultados a obtener.
     * @param pageSize El número de resultados por página.
     */
    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20 // Traemos 20 juegos a la vez
    ): Response<GamesResponse>
}
