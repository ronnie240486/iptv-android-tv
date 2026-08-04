package com.iptv.tv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

val Gold = Color(0xFFFFD700)
val GoldDark = Color(0xFFFFA500)
val Background = Color(0xFF0D1221)
val Surface = Color(0xFF141929)
val SurfaceVariant = Color(0xFF1E2640)
val OnBackground = Color(0xFFFFFFFF)
val OnSurface = Color(0xFFE0E0E0)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun IPTVTheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorScheme(
        primary = Gold,
        onPrimary = Color.Black,
        secondary = GoldDark,
        background = Background,
        surface = Surface,
        surfaceVariant = SurfaceVariant,
        onBackground = OnBackground,
        onSurface = OnSurface,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
