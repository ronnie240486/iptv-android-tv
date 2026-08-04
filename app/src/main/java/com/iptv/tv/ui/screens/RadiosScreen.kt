package com.iptv.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.*
import com.iptv.tv.ui.components.TVSidebar
import com.iptv.tv.ui.navigation.Screen
import com.iptv.tv.ui.theme.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RadiosScreen(navController: NavController, onPlay: (String, String) -> Unit) {
    // Built-in radio list - can be expanded
    val radios = remember {
        listOf(
            Pair("Rádio Globo", "https://playerservices.streamtheworld.com/api/livestream-redirect/RDGLOBO.mp3"),
            Pair("CBN", "https://playerservices.streamtheworld.com/api/livestream-redirect/CBN_SPAAC.mp3"),
            Pair("Jovem Pan", "https://playerservices.streamtheworld.com/api/livestream-redirect/JOVEM_PANAAC.mp3"),
            Pair("Mix FM", "https://playerservices.streamtheworld.com/api/livestream-redirect/MIXFMAAC.mp3"),
        )
    }

    Row(modifier = Modifier.fillMaxSize().background(Background)) {
        TVSidebar(currentRoute = Screen.Radios.route, onNavigate = { navController.navigate(it) })
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("📻 Rádios", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Gold)
            Spacer(Modifier.height(12.dp))
            TvLazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(radios.size) { i ->
                    val (name, url) = radios[i]
                    Surface(
                        onClick = { onPlay(url, name) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = Surface,
                            focusedContainerColor = Gold.copy(0.2f)
                        ),
                        border = ClickableSurfaceDefaults.border(
                            focusedBorder = Border(BorderStroke(2.dp, Gold))
                        )
                    ) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("📻", fontSize = 28.sp)
                            Text(name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
