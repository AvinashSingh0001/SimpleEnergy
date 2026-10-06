package com.example.simpleenergy.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0B6E4F),
    onPrimary = Color.White,
    secondary = Color(0xFF2E933C),
    surface = Color(0xFFF5F7F6),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4CD4A8),
    onPrimary = Color(0xFF003824),
    secondary = Color(0xFF7BD389),
)

@Composable
fun SimpleEnergyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
