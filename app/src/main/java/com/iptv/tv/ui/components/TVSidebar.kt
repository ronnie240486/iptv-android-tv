package com.iptv.tv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.iptv.tv.ui.navigation.Screen
import com.iptv.tv.ui.theme.*
import androidx.compose.ui.graphics.Color

data class NavItem(val emoji: String, val label: String, val route: String)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TVSidebar(currentRoute: String, onNavigate: (String) -> Unit) {
    val items = listOf(
        NavItem("🏠", "Home", Screen.Home.route),
        NavItem("📺", "TV ao Vivo", Screen.LiveTV.route),
        NavItem("🎬", "Filmes", Screen.Movies.route),
        NavItem("📺", "Séries", Screen.Series.route),
        NavItem("📻", "Rádios", Screen.Radios.route),
        NavItem("🔍", "Busca", Screen.Search.route),
        NavItem("📅", "EPG", Screen.EPG.route),
        NavItem("🎲", "Roleta", Screen.Roulette.route),
        NavItem("🤖", "Cine IA", Screen.CineIA.route),
        NavItem("⚙️", "Config", Screen.Settings.route),
    )

    Column(
        modifier = Modifier
            .width(72.dp)
            .fillMaxHeight()
            .background(Surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top)
    ) {
        Spacer(Modifier.height(16.dp))
        Text("📺", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
        Spacer(Modifier.height(8.dp))
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            Surface(
                onClick = { if (!isSelected) onNavigate(item.route) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = if (isSelected) Gold else Color.Transparent,
                    focusedContainerColor = Gold.copy(alpha = 0.7f)
                ),
                shape = ClickableSurfaceDefaults.shape(shape = MaterialTheme.shapes.small)
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(item.emoji, fontSize = 20.sp)
                    Text(
                        item.label,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.Black else Color.White
                    )
                }
            }
        }
    }
}
