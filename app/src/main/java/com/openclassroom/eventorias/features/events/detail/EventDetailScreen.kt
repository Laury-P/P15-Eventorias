package com.openclassroom.eventorias.features.events.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun EventDetailScreen(
    eventId : String,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: EventDetailViewModel = hiltViewModel()
){
    Text(text = "Detail de l'evenment avec l'ID: $eventId" )
}