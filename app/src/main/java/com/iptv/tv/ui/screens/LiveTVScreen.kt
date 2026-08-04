package com.iptv.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.*
import coil.compose.AsyncImage
import com.iptv.tv.data.api.XtreamRepository
import com.iptv.tv.data.model.Category
import com.iptv.tv.data.model.Channel
import com.iptv.tv.ui.components.TVSidebar
import com.iptv.tv.ui.navigation.Screen
import com.iptv.tv.ui.theme.*
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LiveTVScreen(navController: NavController, onPlayChannel: (String, String) -> Unit) {
    val context = LocalContext.current
    val repo = remember { XtreamRepository(context) }
    val scope = rememberCoroutineScope()

    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            categories = repo.getLiveCategories()
            channels = repo.getLiveChannels()
            isLoading = false
        }
    }

    LaunchedEffect(selectedCategory) {
        selectedCategory?.let {
            scope.launch {
                isLoading = true
                channels = repo.getLiveChannels(it.id)
                isLoading = false
            }
        }
    }

    val filtered = if (searchQuery.isEmpty()) channels
    else channels.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Row(modifier = Modifier.fillMaxSize().background(Background)) {
        TVSidebar(currentRoute = Screen.LiveTV.route, onNavigate = { navController.navigate(it) })

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("📺 TV ao Vivo", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Gold)
            Spacer(Modifier.height(8.dp))

            // Category row
            TvLazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(40.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        modifier = Modifier.height(36.dp)
                    ) { Text("Todos") }
                }
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory?.id == cat.id,
                        onClick = { selectedCategory = cat },
                        modifier = Modifier.height(36.dp)
                    ) { Text(cat.name) }
                }
            }

            Spacer(Modifier.height(8.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator(color = Gold)
                }
            } else {
                TvLazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(filtered) { channel ->
                        Surface(
                            onClick = { onPlayChannel(channel.streamUrl, channel.name) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ClickableSurfaceDefaults.colors(
                                containerColor = Surface,
                                focusedContainerColor = Gold.copy(alpha = 0.2f)
                            ),
                            border = ClickableSurfaceDefaults.border(
                                focusedBorder = Border(BorderStroke(2.dp, Gold))
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = channel.logo,
                                    contentDescription = channel.name,
                                    modifier = Modifier.size(48.dp)
                                )
                                Column {
                                    Text(channel.name, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(channel.categoryName, fontSize = 12.sp, color = Color.White.copy(0.5f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
