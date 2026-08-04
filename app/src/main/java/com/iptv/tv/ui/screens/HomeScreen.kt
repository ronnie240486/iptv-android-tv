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
fun HomeScreen(navController: NavController) {
    Row(modifier = Modifier.fillMaxSize().background(Background)) {
        TVSidebar(
            currentRoute = Screen.Home.route,
            onNavigate = { route -> navController.navigate(route) }
        )
        // Main content
        TvLazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    "Bem-vindo ao IPTV TV",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Gold
                )
                Text(
                    "Selecione uma opção no menu",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            item {
                HomeMenuGrid(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeMenuGrid(navController: NavController) {
    val items = listOf(
        Triple("📺", "TV ao Vivo", Screen.LiveTV.route),
        Triple("🎬", "Filmes", Screen.Movies.route),
        Triple("📺", "Séries", Screen.Series.route),
        Triple("📻", "Rádios", Screen.Radios.route),
        Triple("🔍", "Busca", Screen.Search.route),
        Triple("📅", "EPG", Screen.EPG.route),
        Triple("🎲", "Roleta", Screen.Roulette.route),
        Triple("🤖", "Cine IA", Screen.CineIA.route),
        Triple("⚙️", "Configurações", Screen.Settings.route),
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach { (emoji, label, route) ->
            Surface(
                onClick = { navController.navigate(route) },
                modifier = Modifier.width(160.dp).height(120.dp),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = Surface,
                    focusedContainerColor = Gold
                ),
                shape = ClickableSurfaceDefaults.shape(shape = MaterialTheme.shapes.medium)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(emoji, fontSize = 32.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        color = Color.White)
                }
            }
        }
    }
}
