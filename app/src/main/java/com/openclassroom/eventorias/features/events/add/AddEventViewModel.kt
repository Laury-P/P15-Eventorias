package com.openclassroom.eventorias.features.events.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.openclassroom.eventorias.core.domain.repository.EventRepository
import com.openclassroom.eventorias.features.events.detail.FormEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class AddEventViewModel @Inject constructor(eventRepository: EventRepository): ViewModel() {

    private val _newEvent = MutableStateFlow(NewEvent())
    val newEvent: StateFlow<NewEvent> = _newEvent

    fun onAction(formEvent: FormEvent) {
        when (formEvent) {
            is FormEvent.DateChanged -> {
                _newEvent.value = _newEvent.value.copy(date = formEvent.date)
            }
            is FormEvent.AddressChanged -> {
                _newEvent.value = _newEvent.value.copy(address = formEvent.address)
            }
            is FormEvent.DescriptionChanged -> {
                _newEvent.value = _newEvent.value.copy(description = formEvent.description)
            }
            is FormEvent.PhotoSelected -> {
                _newEvent.value = _newEvent.value.copy(pictureUri = formEvent.uri)
            }
            is FormEvent.TimeChanged -> {
                _newEvent.value = _newEvent.value.copy(time = formEvent.time)
            }
            is FormEvent.TitleChanged -> {
                _newEvent.value = _newEvent.value.copy(title = formEvent.title)
            }
            FormEvent.OnSaveClicked -> addEvent()
        }
    }
}

fun addEvent(){

}

data class NewEvent(
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val address: String = "",
    val pictureUri: Uri? = null,
)