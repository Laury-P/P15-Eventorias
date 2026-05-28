package com.openclassroom.eventorias.features.auth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>(start = true)
@Composable
fun AuthScreen (navigator: DestinationsNavigator) {
    Text(text = "Verification que la nav de base fonctionne")
}