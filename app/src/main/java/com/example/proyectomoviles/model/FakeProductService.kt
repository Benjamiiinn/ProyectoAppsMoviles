package com.example.proyectomoviles.model

object FakeProductService {

    private val productos = listOf(
        Producto(
            id = 1,
            nombre = "The Witcher 3: Wild Hunt",
            descripcion = "Un juego de rol de mundo abierto con una historia increíble.",
            precio = 24990.0,
            plataforma = "PC",
            imagenUrl = "https://i.imgur.com/8a1cR8c.jpeg",
            stock = 15
        ),
        Producto(
            id = 2,
            nombre = "Red Dead Redemption 2",
            descripcion = "Vive la vida de un forajido en el corazón de América.",
            precio = 34990.0,
            plataforma = "PS5",
            imagenUrl = "https://i.imgur.com/S51fO9b.jpeg",
            stock = 8
        ),
        Producto(
            id = 3,
            nombre = "The Legend of Zelda: Breath of the Wild",
            descripcion = "Explora el vasto reino de Hyrule en esta aventura épica.",
            precio = 49990.0,
            plataforma = "Nintendo Switch",
            imagenUrl = "https://i.imgur.com/sYmVpM5.jpeg",
            stock = 12
        ),
        Producto(
            id = 4,
            nombre = "Halo Infinite",
            descripcion = "El Jefe Maestro regresa en la campaña más expansiva de Halo hasta la fecha.",
            precio = 44990.0,
            plataforma = "Xbox Series X",
            imagenUrl = "https://i.imgur.com/k9b6vjW.jpeg",
            stock = 5
        ),
        Producto(
            id = 5,
            nombre = "Cyberpunk 2077",
            descripcion = "Un RPG de acción y aventura de mundo abierto ambientado en Night City.",
            precio = 29990.0,
            plataforma = "PC",
            imagenUrl = "https://i.imgur.com/rLdYxYp.jpeg",
            stock = 20
        ),
        Producto(
            id = 6,
            nombre = "God of War Ragnarök",
            descripcion = "Kratos y Atreus se embarcan en un viaje mítico en busca de respuestas.",
            precio = 59990.0,
            plataforma = "PS5",
            imagenUrl = "https://i.imgur.com/a7hA6fP.jpeg",
            stock = 0 // Producto sin stock
        ),
        Producto(
            id = 7,
            nombre = "Elden Ring",
            descripcion = "Un vasto mundo de fantasía oscura te espera en la nueva obra de FromSoftware.",
            precio = 54990.0,
            plataforma = "PC",
            imagenUrl = "https://i.imgur.com/pA4G3c2.jpeg",
            stock = 10
        ),
        Producto(
            id = 8,
            nombre = "Marvel's Spider-Man 2",
            descripcion = "Juega como Peter Parker y Miles Morales mientras se enfrentan a la prueba definitiva.",
            precio = 64990.0,
            plataforma = "PS5",
            imagenUrl = "https://i.imgur.com/Wb3s3p7.jpeg",
            stock = 7
        ),
        Producto(
            id = 9,
            nombre = "Forza Horizon 5",
            descripcion = "Explora los vibrantes y siempre cambiantes paisajes de mundo abierto de México.",
            precio = 49990.0,
            plataforma = "Xbox Series X",
            imagenUrl = "https://i.imgur.com/b2mF8Q1.jpeg",
            stock = 18
        ),
        Producto(
            id = 10,
            nombre = "Super Mario Bros. Wonder",
            descripcion = "¡Espera lo inesperado en la próxima evolución de la diversión de Mario en 2D!",
            precio = 49990.0,
            plataforma = "Nintendo Switch",
            imagenUrl = "https://i.imgur.com/sFLg1P6.jpeg",
            stock = 25
        ),
        Producto(
            id = 11,
            nombre = "Baldur's Gate 3",
            descripcion = "Reúne a tu grupo y vuelve a los Reinos Olvidados en una historia de compañerismo y traición.",
            precio = 49990.0,
            plataforma = "PC",
            imagenUrl = "https://i.imgur.com/G2N5Y5S.jpeg",
            stock = 3
        ),
        Producto(
            id = 12,
            nombre = "Starfield",
            descripcion = "El primer universo nuevo en más de 25 años de Bethesda Game Studios.",
            precio = 59990.0,
            plataforma = "Xbox Series X",
            imagenUrl = "https://i.imgur.com/9n9g7tZ.jpeg",
            stock = 9
        )
    )

    fun getProductos(): List<Producto> {
        return productos
    }
}