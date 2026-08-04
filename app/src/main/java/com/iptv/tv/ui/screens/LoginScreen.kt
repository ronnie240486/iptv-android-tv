package com.iptv.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.iptv.tv.data.api.XtreamRepository
import com.iptv.tv.data.model.ConnectionType
import com.iptv.tv.data.model.ServerConfig
import com.iptv.tv.ui.theme.Gold
import com.iptv.tv.ui.theme.Background
import com.iptv.tv.ui.theme.Surface
import kotlinx.coroutines.launch

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val repo = remember { XtreamRepository(context) }
    val scope = rememberCoroutineScope()

    var tab by remember { mutableStateOf(0) } // 0=Xtream, 1=M3U
    var serverUrl by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var m3uUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (repo.isConfigured()) onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1A2445), Background),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(520.dp)
        ) {
            // Logo
            Text(
                "📺 IPTV TV",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = Gold
            )
            Text("Acesso Premium", fontSize = 16.sp, color = Color.White.copy(alpha = 0.6f))

            Spacer(Modifier.height(32.dp))

            // Tab selector
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { tab = 0 },
                    colors = if (tab == 0) ButtonDefaults.colors(containerColor = Gold)
                    else ButtonDefaults.colors(containerColor = Surface)
                ) { Text("Xtream Codes", color = if (tab == 0) Color.Black else Color.White) }
                Button(
                    onClick = { tab = 1 },
                    colors = if (tab == 1) ButtonDefaults.colors(containerColor = Gold)
                    else ButtonDefaults.colors(containerColor = Surface)
                ) { Text("Lista M3U", color = if (tab == 1) Color.Black else Color.White) }
            }

            Spacer(Modifier.height(24.dp))

            if (tab == 0) {
                // Xtream form
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = serverUrl,
                        onValueChange = { serverUrl = it },
                        label = { Text("URL do Servidor") },
                        leadingIcon = { Icon(Icons.Default.Public, null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                    )
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuário") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                // M3U form
                OutlinedTextField(
                    value = m3uUrl,
                    onValueChange = { m3uUrl = it },
                    label = { Text("URL da Lista M3U") },
                    leadingIcon = { Icon(Icons.Default.Public, null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                )
            }

            if (error.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(error, color = Color(0xFFFF4757), fontSize = 13.sp)
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        error = ""
                        try {
                            val config = if (tab == 0) ServerConfig(
                                serverUrl = serverUrl.trim(),
                                username = username.trim(),
                                password = password.trim(),
                                type = ConnectionType.XTREAM
                            ) else ServerConfig(
                                m3uUrl = m3uUrl.trim(),
                                type = ConnectionType.M3U
                            )
                            repo.saveConfig(config)
                            onLoginSuccess()
                        } catch (e: Exception) {
                            error = "Erro: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.colors(containerColor = Gold)
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.Black)
                else Text("ENTRAR", fontWeight = FontWeight.Black, color = Color.Black, fontSize = 16.sp)
            }
        }
    }
}
