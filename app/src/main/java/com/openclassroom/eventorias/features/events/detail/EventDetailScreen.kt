package com.openclassroom.eventorias.features.events.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassroom.eventorias.features.events.detail.model.DetailEventUiState
import com.openclassroom.eventorias.features.events.eventList.component.LoadingComponent
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
    val detailState by viewModel.uiState.collectAsStateWithLifecycle()

    Column() {
        Text(text = "Detail de l'evenment avec l'ID: $eventId")
        when(val currentState = detailState) {
            is DetailEventUiState.Loading -> LoadingComponent()
            is DetailEventUiState.Error -> Text(text = "error")
            is DetailEventUiState.Success -> Text(text = "Title ${currentState.eventDetail.event.title}")
        }

    }




}