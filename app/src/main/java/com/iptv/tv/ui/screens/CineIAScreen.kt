package com.iptv.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.material3.*
import coil.compose.AsyncImage
import com.iptv.tv.data.api.XtreamRepository
import com.iptv.tv.data.model.Movie
import com.iptv.tv.ui.components.TVSidebar
import com.iptv.tv.ui.navigation.Screen
import com.iptv.tv.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CineIAScreen(navController: NavController, onPlay: (String, String) -> Unit) {
    val context = LocalContext.current
    val repo = remember { XtreamRepository(context) }
    val scope = rememberCoroutineScope()
    var suggestions by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var genre by remember { mutableStateOf("Ação") }

    val genres = listOf("Ação", "Comédia", "Drama", "Terror", "Ficção", "Romance", "Animação")

    fun loadByGenre(g: String) {
        scope.launch {
            isLoading = true
            val all = repo.getMovies()
            suggestions = all.filter { it.genre.contains(g, true) || it.name.contains(g, true) }
                .shuffled().take(12)
            if (suggestions.isEmpty()) suggestions = all.shuffled().take(12)
            isLoading = false
        }
    }

    LaunchedEffect(Unit) { loadByGenre(genre) }

    Row(modifier = Modifier.fillMaxSize().background(Background)) {
        TVSidebar(currentRoute = Screen.CineIA.route, onNavigate = { navController.navigate(it) })
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("🤖 Cine IA", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Gold)
            Text("Sugestões inteligentes por gênero", fontSize = 13.sp, color = Color.White.copy(0.5f))
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                genres.forEach { g ->
                    FilterChip(
                        selected = genre == g,
                        onClick = { genre = g; loadByGenre(g) },
                        modifier = Modifier.height(36.dp)
                    ) { Text(g) }
                }
            }
            Spacer(Modifier.height(12.dp))
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Gold)
                }
            } else {
                TvLazyVerticalGrid(
                    columns = TvGridCells.Fixed(6),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(suggestions) { movie ->
                        Surface(
                            onClick = { onPlay(movie.streamUrl, movie.name) },
                            modifier = Modifier.aspectRatio(2f/3f),
                            colors = ClickableSurfaceDefaults.colors(containerColor = Surface),
                            border = ClickableSurfaceDefaults.border(focusedBorder = Border(BorderStroke(2.dp, Gold)))
                        ) {
                            Box {
                                AsyncImage(model = movie.cover, contentDescription = movie.name,
                                    contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                Box(modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth()
                                    .background(Color.Black.copy(0.7f)).padding(4.dp)) {
                                    Text(movie.name, fontSize = 10.sp, color = Color.White,
                                        maxLines = 2, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
