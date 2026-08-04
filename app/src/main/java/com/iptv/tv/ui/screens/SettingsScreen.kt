package com.iptv.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.*
import com.iptv.tv.data.api.XtreamRepository
import com.iptv.tv.ui.components.TVSidebar
import com.iptv.tv.ui.navigation.Screen
import com.iptv.tv.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val repo = remember { XtreamRepository(context) }
    val scope = rememberCoroutineScope()
    var config by remember { mutableStateOf<com.iptv.tv.data.model.ServerConfig?>(null) }

    LaunchedEffect(Unit) {
        repo.configFlow.collect { config = it }
    }

    Row(modifier = Modifier.fillMaxSize().background(Background)) {
        TVSidebar(currentRoute = Screen.Settings.route, onNavigate = { navController.navigate(it) })
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text("⚙️ Configurações", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Gold)
            Spacer(Modifier.height(24.dp))
            TvLazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    SettingCard("🖥️ Servidor", config?.serverUrl ?: "Não configurado")
                }
                item {
                    SettingCard("👤 Usuário", config?.username ?: "-")
                }
                item {
                    SettingCard("🔌 Tipo", config?.type?.name ?: "-")
                }
                item {
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                repo.saveConfig(com.iptv.tv.data.model.ServerConfig())
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        },
                        colors = ButtonDefaults.colors(containerColor = Color(0xFFFF4757))
                    ) { Text("🚪 Sair / Trocar Servidor", color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingCard(label: String, value: String) {
    Surface(
        onClick = {},
        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
        colors = ClickableSurfaceDefaults.colors(containerColor = com.iptv.tv.ui.theme.Surface)
    ) {
        Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
            Text(label, fontSize = 12.sp, color = Color.White.copy(0.5f))
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
