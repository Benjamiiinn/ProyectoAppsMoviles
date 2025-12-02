package com.example.proyectomoviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
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
            val cartViewModel: CartViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return CartViewModel(productViewModel) as T
                }
            })
            val ordersViewModel: OrdersViewModel = viewModel()

            // --- BYPASS TEMPORAL ELIMINADO ---
            // Volvemos al flujo de autenticación normal.

            var title by remember { mutableStateOf("Login") }D

            Scaffold(
                topBar = {
                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                    val routesWithoutTopBar = setOf("admin", "addProduct", "register", "login", "orders", "confirmation")

                    if (currentRoute !in routesWithoutTopBar) {
                        val showCartIcon = currentRoute?.startsWith("home") == true ||
                            currentRoute?.startsWith("productDetail") == true ||
                            currentRoute == "cart" ||
                            currentRoute == "payment"

                        if (showCartIcon) {
                            MiTopBar(title, cartViewModel, navController)
    S                    }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    // Se establece 'login' como punto de partida.
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
                        AdminScreen(productViewModel, navController)
                    }
                    composable("addProduct") {
                        title = "Agregar Producto"
                        AddProductScreen(navController)
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
