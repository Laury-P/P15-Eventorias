package com.openclassroom.eventorias.features.events.detail

import android.net.Uri
import com.openclassroom.eventorias.core.domain.model.EventCategory


sealed interface FormEvent {
    data class TitleChanged(val title : String) : FormEvent

    data class DescriptionChanged(val description : String) : FormEvent

    data class DateChanged(val date : String) : FormEvent

    data class TimeChanged(val time : String) : FormEvent

    data class AddressChanged (val address : String) : FormEvent

    data class PhotoSelected(val uri : Uri) : FormEvent

    data class CategoryChanged(val category: EventCategory) : FormEvent

    data object OnSaveClicked : FormEvent
}