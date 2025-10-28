package com.example.proyectomoviles.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectomoviles.model.FakeProductService
import com.example.proyectomoviles.model.Producto

class ProductViewModel : ViewModel() {

    // El ViewModel ahora observa directamente la lista mutable del servicio
    val productos: List<Producto> get() = FakeProductService.productos

    // Expone la función para actualizar el stock
    fun updateStock(productId: Int, newStock: Int) {
        FakeProductService.updateStock(productId, newStock)
    }
}
