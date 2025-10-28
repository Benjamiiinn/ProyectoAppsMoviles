package com.example.proyectomoviles.model

import androidx.compose.runtime.mutableStateListOf

object FakeProductService {

    // Convertimos la lista a una lista mutable y observable por Compose
    private val _productos = mutableStateListOf(
        Producto(
            id = 1,
            nombre = "The Witcher 3: Wild Hunt",
            descripcion = "Un juego de rol de mundo abierto con una historia increíble.",
            precio = 24990.0,
            plataforma = "PC",
            imagenUrl = "https://www.thewitcher.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Ftw3.f87ded81.jpg&w=1920&q=75",
            stock = 15
        ),
        Producto(
            id = 2,
            nombre = "Red Dead Redemption 2",
            descripcion = "Vive la vida de un forajido en el corazón de América.",
            precio = 34990.0,
            plataforma = "PS5",
            imagenUrl = "https://image.api.playstation.com/vulcan/img/rnd/202011/1215/WyHa1BM3ISDVqYSEUMB9VZJs.png",
            stock = 8
        ),
        Producto(
            id = 3,
            nombre = "The Legend of Zelda: Breath of the Wild",
            descripcion = "Explora el vasto reino de Hyrule en esta aventura épica.",
            precio = 49990.0,
            plataforma = "Nintendo Switch",
            imagenUrl = "https://www.nintendo.com/eu/media/images/10_share_images/games_15/wiiu_14/SI_WiiU_TheLegendOfZeldaBreathOfTheWild_image1600w.jpg",
            stock = 12
        ),
        Producto(
            id = 4,
            nombre = "Halo Infinite",
            descripcion = "El Jefe Maestro regresa en la campaña más expansiva de Halo hasta la fecha.",
            precio = 44990.0,
            plataforma = "Xbox Series X",
            imagenUrl = "https://store-images.s-microsoft.com/image/apps.50670.13727851868390641.c9cc5f66-aff8-406c-af6b-440838730be0.d205e025-5444-4eb1-ae46-571ae6895928?q=90&w=480&h=270",
            stock = 5
        ),
        Producto(
            id = 5,
            nombre = "Cyberpunk 2077",
            descripcion = "Un RPG de acción y aventura de mundo abierto ambientado en Night City.",
            precio = 29990.0,
            plataforma = "PC",
            imagenUrl = "https://image.api.playstation.com/vulcan/ap/rnd/202111/3013/bxSj4jO0KBqUgAbH3zuNjCje.jpg",
            stock = 20
        ),
        Producto(
            id = 6,
            nombre = "God of War Ragnarök",
            descripcion = "Kratos y Atreus se embarcan en un viaje mítico en busca de respuestas.",
            precio = 59990.0,
            plataforma = "PS5",
            imagenUrl = "https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/2322010/header.jpg?t=1750909504",
            stock = 0 // Producto sin stock
        ),
        Producto(
            id = 7,
            nombre = "Elden Ring",
            descripcion = "Un vasto mundo de fantasía oscura te espera en la nueva obra de FromSoftware.",
            precio = 54990.0,
            plataforma = "PC",
            imagenUrl = "https://image.api.playstation.com/vulcan/ap/rnd/202110/2000/YMUoJUYNX0xWk6eTKuZLr5Iw.jpg",
            stock = 10
        ),
        Producto(
            id = 8,
            nombre = "Marvel\'s Spider-Man 2",
            descripcion = "Juega como Peter Parker y Miles Morales mientras se enfrentan a la prueba definitiva.",
            precio = 64990.0,
            plataforma = "PS5",
            imagenUrl = "https://image.api.playstation.com/vulcan/ap/rnd/202306/1219/2028edeaf4c0b60142550a3d6e024b6009853ceb9f51591e.jpg",
            stock = 7
        ),
        Producto(
            id = 9,
            nombre = "Forza Horizon 5",
            descripcion = "Explora los vibrantes y siempre cambiantes paisajes de mundo abierto de México.",
            precio = 49990.0,
            plataforma = "Xbox Series X",
            imagenUrl = "https://image.api.playstation.com/vulcan/ap/rnd/202501/2717/42b3ee6b1b2094212231b0b0a82824f687fc5c4dc9bde31c.png",
            stock = 18
        ),
        Producto(
            id = 10,
            nombre = "Super Mario Bros. Wonder",
            descripcion = "¡Espera lo inesperado en la próxima evolución de la diversión de Mario en 2D!",
            precio = 49990.0,
            plataforma = "Nintendo Switch",
            imagenUrl = "https://assets.nintendo.com/image/upload/ar_16:9,c_lpad,w_930/b_white/f_auto/q_auto/store/software/switch/70010000068688/1c5583f6bbce5bccdc923c25c35ba8f42128b55df84f4a2fbeea74b6d1d1516e",
            stock = 25
        ),
        Producto(
            id = 11,
            nombre = "Baldur\'s Gate 3",
            descripcion = "Reúne a tu grupo y vuelve a los Reinos Olvidados en una historia de compañerismo y traición.",
            precio = 49990.0,
            plataforma = "PC",
            imagenUrl = "https://image.api.playstation.com/vulcan/ap/rnd/202302/2321/3098481c9164bb5f33069b37e49fba1a572ea3b89971ee7b.jpg",
            stock = 3
        ),
        Producto(
            id = 12,
            nombre = "Starfield",
            descripcion = "El primer universo nuevo en más de 25 años de Bethesda Game Studios.",
            precio = 59990.0,
            plataforma = "Xbox Series X",
            imagenUrl = "https://store-images.s-microsoft.com/image/apps.52870.13567343664224659.1eb6fdf9-8a0b-4344-a135-ab17dfa3c609.20ed1644-2c01-4d5a-b636-3d54ac941a1f?q=90&w=480&h=270",
            stock = 9
        )
    )

    val productos: List<Producto> get() = _productos

    fun updateStock(productId: Int, newStock: Int) {
        val index = _productos.indexOfFirst { it.id == productId }
        if (index != -1) {
            val oldProduct = _productos[index]
            _productos[index] = oldProduct.copy(stock = newStock)
        }
    }
}
