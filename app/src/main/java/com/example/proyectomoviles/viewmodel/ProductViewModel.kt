package com.example.proyectomoviles.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.BuildConfig
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.remote.GameDetailFromApi
import com.example.proyectomoviles.remote.GameFromApi
import com.example.proyectomoviles.remote.RAWGApiService
import com.example.proyectomoviles.remote.RAWGRetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.random.Random

class ProductViewModel : ViewModel() {

    // --- Estados para la lista de productos (HomeScreen) ---
    var productos by mutableStateOf<List<Producto>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    // --- Estados para el detalle de un producto (ProductDetailScreen) ---
    var selectedProduct by mutableStateOf<Producto?>(null)
        private set
    var detailsIsLoading by mutableStateOf(false)
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
                    productos = response.body()!!.results.map { mapToProducto(it) }
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

    fun fetchProductDetails(productId: Int) {
        detailsIsLoading = true
        viewModelScope.launch {
            try {
                val response = apiService.getGameDetails(id = productId, apiKey = BuildConfig.RAWG_API_KEY)
                if (response.isSuccessful && response.body() != null) {
                    selectedProduct = mapToProducto(response.body()!!)
                } else {
                    // Manejar error si no se encuentran los detalles
                }
            } catch (e: Exception) {
                // Manejar error de red
            } finally {
                detailsIsLoading = false
            }
        }
    }

    fun addProduct(newProduct: Producto) {
        productos = listOf(newProduct) + productos
    }

    private fun mapToProducto(game: GameFromApi): Producto {
        return Producto(
            id = game.id,
            nombre = game.name,
            descripcion = "", // La descripción real se cargará en la pantalla de detalles
            precio = Random.nextDouble(19.99, 69.99) * 950,
            stock = Random.nextInt(5, 50),
            plataforma = "Multiplataforma",
            imagenUrl = game.background_image ?: ""
        )
    }

    private fun mapToProducto(game: GameDetailFromApi): Producto {
        return Producto(
            id = game.id,
            nombre = game.name,
            descripcion = game.description_raw, // Usamos la descripción completa de la API
            precio = Random.nextDouble(19.99, 69.99) * 950, 
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
