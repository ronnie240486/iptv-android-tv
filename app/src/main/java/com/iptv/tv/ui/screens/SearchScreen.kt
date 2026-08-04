package com.iptv.tv.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.tv.material3.*
import com.iptv.tv.data.api.XtreamRepository
import com.iptv.tv.ui.components.TVSidebar
import com.iptv.tv.ui.navigation.Screen
import com.iptv.tv.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, onPlay: (String, String) -> Unit) {
    val context = LocalContext.current
    val repo = remember { XtreamRepository(context) }
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Pair<String,String>>>(emptyList()) }

    val voiceLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val text = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull() ?: ""
            query = text
            scope.launch {
                val channels = repo.getLiveChannels().filter { it.name.contains(text, true) }
                    .map { Pair(it.name, it.streamUrl) }
                val movies = repo.getMovies().filter { it.name.contains(text, true) }
                    .map { Pair(it.name, it.streamUrl) }
                results = (channels + movies).take(20)
            }
        }
    }

    Row(modifier = Modifier.fillMaxSize().background(Background)) {
        TVSidebar(currentRoute = Screen.Search.route, onNavigate = { navController.navigate(it) })
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("🔍 Busca Global", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Gold)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { q -> query = q
                        scope.launch {
                            val channels = repo.getLiveChannels().filter { it.name.contains(q, true) }.map { Pair(it.name, it.streamUrl) }
                            results = channels.take(20)
                        }
                    },
                    label = { Text("Buscar...") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale o nome do canal ou filme")
                        }
                        voiceLauncher.launch(intent)
                    },
                    colors = ButtonDefaults.colors(containerColor = Gold)
                ) { Text("🎙️ Voz", color = Color.Black, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.height(16.dp))
            results.forEach { (name, url) ->
                Surface(
                    onClick = { onPlay(url, name) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ClickableSurfaceDefaults.colors(containerColor = Surface, focusedContainerColor = Gold.copy(0.2f)),
                    border = ClickableSurfaceDefaults.border(focusedBorder = Border(BorderStroke(2.dp, Gold)))
                ) {
                    Text(name, modifier = Modifier.padding(12.dp), color = Color.White)
                }
            }
        }
    }
}
