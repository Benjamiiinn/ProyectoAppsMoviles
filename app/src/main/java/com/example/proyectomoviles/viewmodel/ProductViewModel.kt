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

    var productos by mutableStateOf<List<Producto>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

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
                    val gameDetails = response.body()!!
                    val existingProduct = productos.find { it.id == productId }

                    if (existingProduct != null) {
                        val descriptionToSet = if (existingProduct.descripcion.isNotBlank()) {
                            existingProduct.descripcion
                        } else {
                            gameDetails.description_raw
                        }
                        
                        val updatedProduct = existingProduct.copy(descripcion = descriptionToSet)
                        selectedProduct = updatedProduct

                        productos = productos.map { if (it.id == productId) updatedProduct else it }
                    } else {
                        val newProduct = mapToProducto(gameDetails)
                        productos = listOf(newProduct) + productos
                        selectedProduct = newProduct
                    }
                } else {
                    // Manejar error
                }
            } catch (e: Exception) {
                // Manejar error
            } finally {
                detailsIsLoading = false
            }
        }
    }

    fun addProduct(newProduct: Producto) {
        productos = listOf(newProduct) + productos
    }

    fun editProduct(updatedProduct: Producto) {
        productos = productos.map {
            if (it.id == updatedProduct.id) updatedProduct else it
        }
    }

    fun deleteProduct(productId: Int) {
        productos = productos.filterNot { it.id == productId }
    }

    fun decreaseStock(productId: Int, quantity: Int): Boolean {
        val product = productos.find { it.id == productId }
        if (product != null && product.stock >= quantity) {
            updateStock(productId, product.stock - quantity)
            return true
        }
        return false
    }

    fun increaseStock(productId: Int, quantity: Int) {
        val product = productos.find { it.id == productId }
        if (product != null) {
            updateStock(productId, product.stock + quantity)
        }
    }

    private fun mapToProducto(game: GameFromApi): Producto {
        return Producto(
            id = game.id,
            nombre = game.name,
            descripcion = "",
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
            descripcion = game.description_raw,
            precio = Random.nextDouble(19.99, 69.99) * 950, 
            stock = Random.nextInt(5, 50),
            plataforma = "Multiplataforma",
            imagenUrl = game.background_image ?: ""
        )
    }

    fun updateStock(productId: Int, newStock: Int) {
        var updatedProduct: Producto? = null
        val updatedList = productos.map {
            if (it.id == productId) {
                val product = it.copy(stock = newStock)
                updatedProduct = product
                product
            } else {
                it
            }
        }
        productos = updatedList

        if (selectedProduct?.id == productId) {
            selectedProduct = updatedProduct
        }
    }
}
