import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.proyectomoviles.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiTopBar(
    title: String,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val cartItems by cartViewModel.cartItems.collectAsState()

    TopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(onClick = { navController.navigate("cart") }) {
                BadgedBox(
                    badge = {
                        if (cartItems.isNotEmpty()) {
                            Badge { Text(cartItems.size.toString()) }
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = "Carrito"
                    )
                }
            }
        }
    )
}