package com.iptv.tv.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.tv.material3.*
import com.iptv.tv.data.api.XtreamRepository
import com.iptv.tv.data.model.Movie
import com.iptv.tv.ui.components.TVSidebar
import com.iptv.tv.ui.navigation.Screen
import com.iptv.tv.ui.theme.*
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RouletteScreen(navController: NavController, onPlay: (String, String) -> Unit) {
    val context = LocalContext.current
    val repo = remember { XtreamRepository(context) }
    val scope = rememberCoroutineScope()
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var picked by remember { mutableStateOf<Movie?>(null) }
    var isSpinning by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) { movies = repo.getMovies() }

    Row(modifier = Modifier.fillMaxSize().background(Background)) {
        TVSidebar(currentRoute = Screen.Roulette.route, onNavigate = { navController.navigate(it) })
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🎲 Roleta de Filmes", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Gold)
            Text("Deixa o acaso escolher!", fontSize = 14.sp, color = Color.White.copy(0.5f))
            Spacer(Modifier.height(32.dp))

            // Roulette emoji spinner
            Text(
                "🎰",
                fontSize = 80.sp,
                modifier = Modifier.rotate(rotation.value)
            )

            Spacer(Modifier.height(32.dp))

            picked?.let { movie ->
                Card(
                    onClick = {},
                    modifier = Modifier.width(240.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
                        AsyncImage(
                            model = movie.cover, contentDescription = movie.name,
                            modifier = Modifier.fillMaxWidth().height(160.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(movie.name, fontWeight = FontWeight.Bold, color = Color.White,
                            textAlign = TextAlign.Center)
                        if (movie.year.isNotEmpty())
                            Text(movie.year, fontSize = 12.sp, color = Color.White.copy(0.5f))
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { onPlay(movie.streamUrl, movie.name) },
                            colors = ButtonDefaults.colors(containerColor = Gold),
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("▶ Assistir", color = Color.Black, fontWeight = FontWeight.Bold) }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!isSpinning && movies.isNotEmpty()) {
                        scope.launch {
                            isSpinning = true
                            rotation.animateTo(
                                targetValue = rotation.value + (720f + (0..360).random()),
                                animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                            )
                            picked = movies.random()
                            isSpinning = false
                        }
                    }
                },
                colors = ButtonDefaults.colors(containerColor = Gold),
                modifier = Modifier.height(52.dp).width(200.dp)
            ) {
                Text(
                    if (isSpinning) "Girando..." else "🎲 Girar Roleta",
                    color = Color.Black, fontWeight = FontWeight.Black, fontSize = 16.sp
                )
            }
        }
    }
}
