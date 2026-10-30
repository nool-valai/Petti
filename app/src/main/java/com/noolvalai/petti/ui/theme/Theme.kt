package com.noolvalai.petti.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalHighContrast = compositionLocalOf { false }
private val DarkColorScheme = darkColorScheme(
    background = SurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceText,
    onBackground = OnSurfaceText
)

@Composable
fun PettiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography, // This links your Montserrat setup
        content = content
    )
}