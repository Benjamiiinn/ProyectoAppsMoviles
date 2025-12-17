package com.example.proyectomoviles.ui.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.proyectomoviles.remote.GameFromApi
import com.example.proyectomoviles.viewmodel.NewsViewModel

@Composable
fun NovedadesScreen(newsViewModel: NewsViewModel = viewModel()) {
    val games = newsViewModel.games
    val isLoading = newsViewModel.isLoading
    val errorMessage = newsViewModel.errorMessage

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(games) { game ->
                    GameCard(game = game)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun GameCard(game: GameFromApi) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Box {
            AsyncImage(
                model = game.background_image,
                contentDescription = "Imagen de ${game.name}",
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            // --- Contenedor para Metacritic y ESRB ---
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // --- Puntuación de Metacritic ---
                game.metacritic?.let {
                    val color = when (it) {
                        in 75..100 -> Color.Green
                        in 50..74 -> Color.Yellow
                        else -> Color.Red
                    }
                    Box(modifier = Modifier.background(color, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text(
                            text = it.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                // --- Clasificación ESRB ---
                game.esrb_rating?.name?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            // --- Título del juego ---
            Text(
                text = game.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}
