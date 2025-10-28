package com.example.proyectomoviles.model

object FakeProductService {

    private val productos = listOf(
        Producto(
            id = 1,
            nombre = "The Witcher 3: Wild Hunt",
            descripcion = "Un juego de rol de mundo abierto con una historia increíble.",
            precio = 29.99,
            plataforma = "PC",
            imagenUrl = "https://i.imgur.com/8a1cR8c.jpeg"
        ),
        Producto(
            id = 2,
            nombre = "Red Dead Redemption 2",
            descripcion = "Vive la vida de un forajido en el corazón de América.",
            precio = 39.99,
            plataforma = "PS5",
            imagenUrl = "https://i.imgur.com/S51fO9b.jpeg"
        ),
        Producto(
            id = 3,
            nombre = "The Legend of Zelda: Breath of the Wild",
            descripcion = "Explora el vasto reino de Hyrule en esta aventura épica.",
            precio = 59.99,
            plataforma = "Nintendo Switch",
            imagenUrl = "https://i.imgur.com/sYmVpM5.jpeg"
        ),
        Producto(
            id = 4,
            nombre = "Halo Infinite",
            descripcion = "El Jefe Maestro regresa en la campaña más expansiva de Halo hasta la fecha.",
            precio = 49.99,
            plataforma = "Xbox Series X",
            imagenUrl = "https://i.imgur.com/k9b6vjW.jpeg"
        ),
        Producto(
            id = 5,
            nombre = "Cyberpunk 2077",
            descripcion = "Un RPG de acción y aventura de mundo abierto ambientado en Night City.",
            precio = 35.50,
            plataforma = "PC",
            imagenUrl = "https://i.imgur.com/rLdYxYp.jpeg"
        ),
        Producto(
            id = 6,
            nombre = "God of War Ragnarök",
            descripcion = "Kratos y Atreus se embarcan en un viaje mítico en busca de respuestas.",
            precio = 69.99,
            plataforma = "PS5",
            imagenUrl = "https://i.imgur.com/a7hA6fP.jpeg"
        )
    )

    fun getProductos(): List<Producto> {
        return productos
    }
}