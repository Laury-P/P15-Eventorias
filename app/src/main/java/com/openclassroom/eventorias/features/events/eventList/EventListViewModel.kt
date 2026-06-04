package com.openclassroom.eventorias.features.events.eventList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassroom.eventorias.features.events.eventList.model.ListEventUiModel
import com.openclassroom.eventorias.features.events.usecases.GetUiEventListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EventListViewModel @Inject constructor(eventListUseCase: GetUiEventListUseCase): ViewModel(){
    private val _eventList : MutableStateFlow<List<ListEventUiModel>> = MutableStateFlow(emptyList())

    val eventList : StateFlow<List<ListEventUiModel>>
        get() = _eventList

    init {
        viewModelScope.launch {
            eventListUseCase().collect {
                _eventList.value = it
            }
        }
    }
}