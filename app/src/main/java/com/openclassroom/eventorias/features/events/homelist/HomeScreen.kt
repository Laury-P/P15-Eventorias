package com.openclassroom.eventorias.features.events.homelist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>
@Composable
fun HomeScreen(
    id: Int = 1,
) {
    Text(text="This is the home screen")
}