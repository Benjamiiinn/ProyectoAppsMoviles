package com.example.proyectomoviles.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// --- Data Classes para la respuesta de la API de RAWG ---

// El objeto principal que devuelve la lista de juegos
data class GamesResponse(
    val results: List<GameFromApi>
)

// Representa un solo juego en una lista (vista simplificada)
data class GameFromApi(
    val id: Int,
    val name: String,
    val background_image: String? // La URL de la imagen puede ser nula
)

// Representa los detalles completos de un solo juego
data class GameDetailFromApi(
    val id: Int,
    val name: String,
    val description_raw: String, // La descripción completa en texto plano
    val background_image: String?
)

// --- Interfaz de Retrofit para la API de RAWG ---

interface RAWGApiService {

    /**
     * Obtiene una lista de juegos.
     */
    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<GamesResponse>

    /**
     * Obtiene los detalles de un juego específico por su ID.
     */
    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") id: Int,
        @Query("key") apiKey: String
    ): Response<GameDetailFromApi>
}
