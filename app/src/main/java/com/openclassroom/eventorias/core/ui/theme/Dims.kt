package com.openclassroom.eventorias.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class EventoriasDimensions(
    val smallPadding : Dp,
    val mediumPadding : Dp,
    val bigPadding : Dp

)

val compactDimensions = EventoriasDimensions(
    smallPadding = 8.dp,
    mediumPadding = 12.dp,
    bigPadding = 16.dp,
)

val LocalAppDimensions = staticCompositionLocalOf { compactDimensions }