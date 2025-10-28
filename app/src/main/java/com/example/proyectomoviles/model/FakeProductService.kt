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
            imagenUrl = "https://www.thewitcher.com/us/es",
            stock = 15
        ),
        Producto(
            id = 2,
            nombre = "Red Dead Redemption 2",
            descripcion = "Vive la vida de un forajido en el corazón de América.",
            precio = 34990.0,
            plataforma = "PS5",
            imagenUrl = "https://store.steampowered.com/agecheck/app/1174180/?l=spanish&curator_clanid=11846346",
            stock = 8
        ),
        Producto(
            id = 3,
            nombre = "The Legend of Zelda: Breath of the Wild",
            descripcion = "Explora el vasto reino de Hyrule en esta aventura épica.",
            precio = 49990.0,
            plataforma = "Nintendo Switch",
            imagenUrl = "https://www.nintendo.com/es-es/Juegos/Juegos-de-Nintendo-Switch/The-Legend-of-Zelda-Breath-of-the-Wild-1173609.html?srsltid=AfmBOopZcpXyHGYCvT1jMiDB8ig00NEYT-ALfdiENhErLHATnCUO61A1",
            stock = 12
        ),
        Producto(
            id = 4,
            nombre = "Halo Infinite",
            descripcion = "El Jefe Maestro regresa en la campaña más expansiva de Halo hasta la fecha.",
            precio = 44990.0,
            plataforma = "Xbox Series X",
            imagenUrl = "https://store.steampowered.com/app/1240440/Halo_Infinite/?l=spanish",
            stock = 5
        ),
        Producto(
            id = 5,
            nombre = "Cyberpunk 2077",
            descripcion = "Un RPG de acción y aventura de mundo abierto ambientado en Night City.",
            precio = 29990.0,
            plataforma = "PC",
            imagenUrl = "https://www.cyberpunk.net/us/es/",
            stock = 20
        ),
        Producto(
            id = 6,
            nombre = "God of War Ragnarök",
            descripcion = "Kratos y Atreus se embarcan en un viaje mítico en busca de respuestas.",
            precio = 59990.0,
            plataforma = "PS5",
            imagenUrl = "https://store.playstation.com/es-cl/product/UP9000-PPSA08329_00-GOWRAGNAROK00000?gclsrc=aw.ds&gad_source=1&gad_campaignid=14766217841&gbraid=0AAAAADc1uAaBVCmAoiwdup6MleqeO2MXw&gclid=Cj0KCQjwsPzHBhDCARIsALlWNG04BDsg-ci3w3km6weHiUwlDavheqXRF6KiNowKH344cKx2Yn3Ujc0aArF9EALw_wcB",
            stock = 0 // Producto sin stock
        ),
        Producto(
            id = 7,
            nombre = "Elden Ring",
            descripcion = "Un vasto mundo de fantasía oscura te espera en la nueva obra de FromSoftware.",
            precio = 54990.0,
            plataforma = "PC",
            imagenUrl = "https://store.steampowered.com/agecheck/app/1245620/?l=spanish",
            stock = 10
        ),
        Producto(
            id = 8,
            nombre = "Marvel\'s Spider-Man 2",
            descripcion = "Juega como Peter Parker y Miles Morales mientras se enfrentan a la prueba definitiva.",
            precio = 64990.0,
            plataforma = "PS5",
            imagenUrl = "https://www.playstation.com/es-cl/games/marvels-spider-man-2/",
            stock = 7
        ),
        Producto(
            id = 9,
            nombre = "Forza Horizon 5",
            descripcion = "Explora los vibrantes y siempre cambiantes paisajes de mundo abierto de México.",
            precio = 49990.0,
            plataforma = "Xbox Series X",
            imagenUrl = "https://www.playstation.com/es-cl/games/forza-horizon-5/",
            stock = 18
        ),
        Producto(
            id = 10,
            nombre = "Super Mario Bros. Wonder",
            descripcion = "¡Espera lo inesperado en la próxima evolución de la diversión de Mario en 2D!",
            precio = 49990.0,
            plataforma = "Nintendo Switch",
            imagenUrl = "https://www.nintendo.com/es-cl/store/products/super-mario-bros-wonder-switch/?srsltid=AfmBOorjTBmzzejwXLIQVldrPj1GrLldSBOuwC6f_kIAK732mKk6F95l",
            stock = 25
        ),
        Producto(
            id = 11,
            nombre = "Baldur\'s Gate 3",
            descripcion = "Reúne a tu grupo y vuelve a los Reinos Olvidados en una historia de compañerismo y traición.",
            precio = 49990.0,
            plataforma = "PC",
            imagenUrl = "https://store.steampowered.com/agecheck/app/1086940/",
            stock = 3
        ),
        Producto(
            id = 12,
            nombre = "Starfield",
            descripcion = "El primer universo nuevo en más de 25 años de Bethesda Game Studios.",
            precio = 59990.0,
            plataforma = "Xbox Series X",
            imagenUrl = "https://store.steampowered.com/agecheck/app/1716740/?l=spanish",
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
