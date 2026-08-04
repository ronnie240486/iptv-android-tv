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
import com.iptv.tv.ui.components.TVSidebar
import com.iptv.tv.ui.navigation.Screen
import com.iptv.tv.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MoviesScreen(navController: NavController, onPlay: (String, String) -> Unit) {
    val context = LocalContext.current
    val repo = remember { XtreamRepository(context) }
    val scope = rememberCoroutineScope()
    var movies by remember { mutableStateOf(emptyList<com.iptv.tv.data.model.Movie>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) { scope.launch { movies = repo.getMovies(); isLoading = false } }

    Row(modifier = Modifier.fillMaxSize().background(Background)) {
        TVSidebar(currentRoute = Screen.Movies.route, onNavigate = { navController.navigate(it) })
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("🎬 Filmes", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Gold)
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
                    items(movies) { movie ->
                        Surface(
                            onClick = { onPlay(movie.streamUrl, movie.name) },
                            modifier = Modifier.aspectRatio(2f/3f),
                            colors = ClickableSurfaceDefaults.colors(
                                containerColor = Surface,
                                focusedContainerColor = Surface
                            ),
                            border = ClickableSurfaceDefaults.border(
                                focusedBorder = Border(BorderStroke(2.dp, Gold))
                            )
                        ) {
                            Box {
                                AsyncImage(
                                    model = movie.cover,
                                    contentDescription = movie.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier.align(Alignment.BottomStart)
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(0.7f))
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        movie.name,
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
