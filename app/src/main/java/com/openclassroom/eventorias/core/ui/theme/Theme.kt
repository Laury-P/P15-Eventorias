package com.openclassroom.eventorias.core.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = red,
    onPrimary = white,

    secondary = white,
    onSecondary = grey,

    tertiary = grey,
    onTertiary = white,

    background = black,
    onBackground = white,

    surface = grey,
    onSurface = lightGrey,
)

@Composable
fun EventoriasTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}