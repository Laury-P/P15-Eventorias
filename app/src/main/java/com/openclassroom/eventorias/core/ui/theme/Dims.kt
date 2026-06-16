package com.openclassroom.eventorias.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class EventoriasDimensions(
    val padding4 : Dp,
    val padding8 : Dp,
    val padding12 : Dp,
    val padding16 : Dp,
    val padding20 : Dp,
    val padding24 : Dp,
    val logScreenPadding : Dp,
    val avatarEventList : Dp,
    val avatarDetail : Dp,
    val cardHeight : Dp,
    val errorIconSize : Dp,
    val detailIconSize : Dp,
)

val compactDimensions = EventoriasDimensions(
    // Event list: Between card (vertical)
    padding4 = 4.dp,
    padding8 = 8.dp,
    padding12 = 12.dp,
    padding16 = 16.dp,
    padding20 = 20.dp,
    // Event list: Horizontal Padding
    padding24 = 24.dp,
    logScreenPadding = 50.dp,
    avatarEventList = 40.dp,
    avatarDetail = 60.dp,
    cardHeight = 80.dp,
    errorIconSize = 64.dp,
    detailIconSize = 24.dp,
)

val LocalAppDimensions = staticCompositionLocalOf { compactDimensions }