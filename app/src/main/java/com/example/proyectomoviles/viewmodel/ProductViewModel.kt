package com.example.proyectomoviles.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.BuildConfig
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.remote.GameFromApi
import com.example.proyectomoviles.remote.RAWGApiService
import com.example.proyectomoviles.remote.RAWGRetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.random.Random

class ProductViewModel : ViewModel() {

    var productos by mutableStateOf<List<Producto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    private val apiService: RAWGApiService by lazy {
        RAWGRetrofitClient.instance.create(RAWGApiService::class.java)
    }

    init {
        fetchProductos()
    }

    fun fetchProductos() {
        isLoading = true
        errorMessage = ""
        viewModelScope.launch {
            try {
                val response = apiService.getGames(apiKey = BuildConfig.RAWG_API_KEY)
                if (response.isSuccessful && response.body() != null) {
                    val gamesFromApi = response.body()!!.results
                    productos = gamesFromApi.map { gameFromApi ->
                        mapToProducto(gameFromApi)
                    }
                } else {
                    errorMessage = "Error al cargar los juegos: ${response.code()}"
                }
            } catch (e: IOException) {
                errorMessage = "No se pudo conectar al servidor de RAWG. Revisa tu conexión."
            } catch (e: Exception) {
                errorMessage = "Ocurrió un error inesperado: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addProduct(newProduct: Producto) {
        productos = listOf(newProduct) + productos
    }

    private fun mapToProducto(game: GameFromApi): Producto {
        // Asumimos que los precios de la API (si los hubiera) están en USD
        // Los convertimos a CLP con un tipo de cambio fijo.
        val usdPrice = Random.nextDouble(19.99, 69.99) // Precio aleatorio en USD
        val clpPrice = usdPrice * 950 // Tipo de cambio fijo (ej: 1 USD = 950 CLP)

        return Producto(
            id = game.id,
            nombre = game.name,
            descripcion = "Un emocionante juego disponible en múltiples plataformas.",
            precio = clpPrice, // Guardamos el precio en CLP
            stock = Random.nextInt(5, 50),
            plataforma = "Multiplataforma",
            imagenUrl = game.background_image ?: ""
        )
    }

    fun updateStock(productId: Int, newStock: Int) {
        val updatedList = productos.map {
            if (it.id == productId) {
                it.copy(stock = newStock)
            } else {
                it
            }
        }
        productos = updatedList
    }
}
