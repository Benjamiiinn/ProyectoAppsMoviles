package com.example.proyectomoviles

import MiTopBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.proyectomoviles.viewmodel.AuthViewModel
import com.example.proyectomoviles.views.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: AuthViewModel = viewModel()

            var title by remember { mutableStateOf("Registro") }

            Scaffold(
                topBar = {
                    MiTopBar(title = title)
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "register",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("register") {
                        title = "Registro"
                        RegisterScreen(navController, viewModel)
                    }
                    composable("login") {
                        title = "Login"
                        LoginScreen(navController, viewModel)
                    }
                    composable("home/{email}") { backStackEntry ->
                        title = "Inicio"
                        val email = backStackEntry.arguments?.getString("email")
                        HomeScreen(email)
                    }
                }
            }
        }
    }
}