package com.example.proyectomoviles.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.model.Genero
import com.example.proyectomoviles.model.Plataforma
import com.example.proyectomoviles.model.Producto
import com.example.proyectomoviles.remote.CreateProductRequest
import com.example.proyectomoviles.remote.ProductAPIService
import com.example.proyectomoviles.remote.RetrofitClient
import com.example.proyectomoviles.remote.UpdateProductRequest
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    var productos by mutableStateOf<List<Producto>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    var generos by mutableStateOf<List<Genero>>(emptyList())
        private set
    var plataformas by mutableStateOf<List<Plataforma>>(emptyList())
        private set
    var selectedGenero by mutableStateOf<Genero?>(null)
    var selectedPlataforma by mutableStateOf<Plataforma?>(null)

    var selectedProduct by mutableStateOf<Producto?>(null)
        private set

    private val apiService: ProductAPIService by lazy {
        RetrofitClient.instance.create(ProductAPIService::class.java)
    }

    init {
        fetchInitialData()
    }

    fun fetchInitialData() {
        isLoading = true
        errorMessage = ""
        viewModelScope.launch {
            try {
                val productsResponse = apiService.getProducts()
                if (productsResponse.isSuccessful && productsResponse.body() != null) {
                    productos = productsResponse.body()!!
                }
                val generosResponse = apiService.getGeneros()
                if (generosResponse.isSuccessful && generosResponse.body() != null) {
                    generos = generosResponse.body()!!
                }
                val plataformasResponse = apiService.getPlataformas()
                if (plataformasResponse.isSuccessful && plataformasResponse.body() != null) {
                    plataformas = plataformasResponse.body()!!
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addProduct(product: Producto, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val request = CreateProductRequest(
                    nombre = product.nombre,
                    descripcion = product.descripcion,
                    precio = product.precio,
                    stock = product.stock,
                    imagenUrl = product.imagenUrl,
                    plataformaId = product.plataforma.id,
                    generoId = product.genero.id
                )
                val response = apiService.createProduct(request)
                if (response.isSuccessful) {
                    fetchInitialData()
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
    
    fun editProduct(product: Producto, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                 val request = UpdateProductRequest(
                    nombre = product.nombre,
                    descripcion = product.descripcion,
                    precio = product.precio,
                    stock = product.stock
                )
                val response = apiService.updateProduct(product.id, request)
                if (response.isSuccessful) {
                    fetchInitialData()
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun deleteProduct(productId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteProduct(productId)
                if (response.isSuccessful) {
                    fetchInitialData()
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun onGeneroSelected(genero: Genero?) {
        selectedGenero = genero
    }

    fun onPlataformaSelected(plataforma: Plataforma?) {
        selectedPlataforma = plataforma
    }

    fun clearFilters() {
        selectedGenero = null
        selectedPlataforma = null
    }

    fun getFilteredProducts(): List<Producto> {
        return productos.filter {
            val product = it
            val matchesGenero = selectedGenero?.let { filterGenero -> product.genero.id == filterGenero.id } ?: true
            val matchesPlataforma = selectedPlataforma?.let { filterPlataforma -> product.plataforma.id == filterPlataforma.id } ?: true
            matchesGenero && matchesPlataforma
        }
    }
}
