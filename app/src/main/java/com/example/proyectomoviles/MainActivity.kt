package com.example.proyectomoviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.proyectomoviles.ui.news.NovedadesScreen
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.CartViewModel
import com.example.proyectomoviles.viewmodel.OrdersViewModel
import com.example.proyectomoviles.viewmodel.ProductViewModel
import com.example.proyectomoviles.views.*

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.proyectomoviles.utils.TokenManager.init(applicationContext)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            val productViewModel: ProductViewModel = viewModel()
            val cartViewModel: CartViewModel = viewModel()
            val ordersViewModel: OrdersViewModel = viewModel()

            var title by remember { mutableStateOf("Login") }

            Scaffold(
                topBar = {
                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                    val routesWithoutTopBar = setOf("login", "register")

                    if (currentRoute !in routesWithoutTopBar) {
                        // La TopBar se mostrará en todas las demás pantallas.
                        MiTopBar(title, cartViewModel, navController)
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "login",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("register") {
                        title = "Registro"
                        RegisterScreen(navController, authViewModel)
                    }
                    composable("login") {
                        title = "Login"
                        LoginScreen(navController, authViewModel)
                    }
                    composable("home/{email}") { backStackEntry ->
                        title = "Inicio"
                        HomeScreen(authViewModel, productViewModel, navController)
                    }
                    composable("novedades") {
                        title = "Novedades"
                        NovedadesScreen()
                    }
                    composable("profile") {
                        title = "Mi Perfil"
                        ProfileScreen(navController, authViewModel)
                    }
                    composable(
                        "productDetail/{productId}",
                        arguments = listOf(navArgument("productId") { type = NavType.IntType })
                    ) {
                        title = "Detalle del Producto"
                        val productId = it.arguments?.getInt("productId") ?: -1
                        ProductDetailScreen(productId, productViewModel, cartViewModel, navController)
                    }
                    composable("cart") {
                        title = "Carrito"
                        CartScreen(authViewModel, cartViewModel, navController)
                    }
                    composable("payment") {
                        title = "Realizar Pago"
                        PaymentScreen(cartViewModel, navController)
                    }
                    composable("confirmation") {
                        title = "Compra Completada"
                        ConfirmationScreen(navController, cartViewModel)
                    }
                    composable("purchaseError") {
                        title = "Error en la Compra"
                        PurchaseErrorScreen(navController)
                    }
                    composable("admin") {
                        title = "Gestión de Productos"
                        // CORREGIDO: Pasamos el AuthViewModel a la AdminScreen
                        AdminScreen(authViewModel, productViewModel, navController)
                    }
                    composable("addProduct") {
                        title = "Agregar Producto"
                        AddProductScreen(navController, productViewModel)
                    }
                    composable("orders") {
                        title = "Mis Pedidos"
                        OrdersScreen(navController, authViewModel, ordersViewModel)
                    }
                }
            }
        }
    }
}
