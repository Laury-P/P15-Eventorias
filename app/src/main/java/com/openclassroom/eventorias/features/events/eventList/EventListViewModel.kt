package com.openclassroom.eventorias.features.events.eventList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassroom.eventorias.features.events.eventList.model.ListEventUiState
import com.openclassroom.eventorias.features.events.usecases.GetUiEventListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@HiltViewModel
class EventListViewModel @Inject constructor(eventListUseCase: GetUiEventListUseCase) :
    ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery : StateFlow<String> = _searchQuery

    private val refreshSignal = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ListEventUiState> = refreshSignal
        .flatMapLatest {
            combine(
                eventListUseCase(),
                _searchQuery.debounce(200)
            ) { result, searchQuery ->
                if (result.isSuccess) {
                    val list = result.getOrNull() ?: emptyList()
                    val filteredList = if (searchQuery.isNotBlank()) {
                        list.filter { uiEvent ->
                            uiEvent.event.title.contains(searchQuery, ignoreCase = true)
                        }
                    } else {
                        list
                    }
                    ListEventUiState.Success(listEvent = filteredList)
                } else {
                    val exception = result.exceptionOrNull()
                    ListEventUiState.Error(
                        message = exception?.localizedMessage ?: "An error occurred"
                    )
                }
            }
        }
        .onStart { emit(ListEventUiState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ListEventUiState.Loading
        )

    fun retry() {
        viewModelScope.launch { refreshSignal.emit(Unit) }
    }

    fun setSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
    }
}