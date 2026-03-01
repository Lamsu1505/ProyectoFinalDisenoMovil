package com.example.proyectofinaldisenomovil.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = whiteBackground,
    primary = green,
    secondary = orange,
    tertiary = red,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White
)

private val LightColorScheme = lightColorScheme(
    background = whiteBackground,
    primary = green,
    secondary = orange,
    tertiary = red,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
)

@Composable
fun ProyectoFinalDisenoMovilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}