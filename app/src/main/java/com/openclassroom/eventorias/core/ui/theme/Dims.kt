package com.openclassroom.eventorias.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class EventoriasDimensions(
    val padding8 : Dp,
    val padding16 : Dp,
    val padding20 : Dp,
    val padding24 : Dp,
    val avatarEventList : Dp,
    val cardHeight : Dp

)

val compactDimensions = EventoriasDimensions(
    // Event list: Between card (vertical)
    padding8 = 8.dp,
    padding16 = 16.dp,

    // Event list: Horizontal Padding
    padding24 = 24.dp,

    padding20 = 20.dp,

    avatarEventList = 40.dp,
    cardHeight = 80.dp
)

val LocalAppDimensions = staticCompositionLocalOf { compactDimensions }