package com.example.proyectomoviles

import MiTopBar
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
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.viewmodel.CartViewModel
import com.example.proyectomoviles.viewmodel.ProductViewModel
import com.example.proyectomoviles.views.*

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            val productViewModel: ProductViewModel = viewModel()
            val cartViewModel: CartViewModel = viewModel()

            var title by remember { mutableStateOf("Registro") }

            Scaffold(
                topBar = {
                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                    val showCartIcon = currentRoute?.startsWith("home") == true ||
                        currentRoute?.startsWith("productDetail") == true ||
                        currentRoute == "cart"

                    if (showCartIcon) {
                        MiTopBar(title, cartViewModel, navController)
                    } else if (currentRoute == "admin") {
                        // Barra simple para el panel de admin
                        TopAppBar(title = { Text(title) })
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "register",
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
                        CartScreen(cartViewModel, navController)
                    }
                    composable("confirmation") {
                        title = "Compra Completada"
                        ConfirmationScreen(navController)
                    }
                    composable("admin") {
                        title = "Administración de Stock"
                        AdminScreen(productViewModel)
                    }
                }
            }
        }
    }
}