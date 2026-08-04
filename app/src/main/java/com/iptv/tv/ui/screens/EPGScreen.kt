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
import androidx.tv.material3.*
import com.iptv.tv.ui.components.TVSidebar
import com.iptv.tv.ui.navigation.Screen
import com.iptv.tv.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun EPGScreen(navController: NavController) {
    val now = remember { Calendar.getInstance() }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale("pt", "BR")) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }

    Row(modifier = Modifier.fillMaxSize().background(Background)) {
        TVSidebar(currentRoute = Screen.EPG.route, onNavigate = { navController.navigate(it) })
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("📅 Guia de Programação (EPG)", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Gold)
            Text("${dateFormat.format(now.time)} — ${timeFormat.format(now.time)}", color = Color.White.copy(0.6f))
            Spacer(Modifier.height(16.dp))
            Text(
                "Configure a URL do EPG nas configurações do servidor para visualizar a grade completa de programação.",
                color = Color.White.copy(0.7f),
                fontSize = 14.sp
            )
        }
    }
}
