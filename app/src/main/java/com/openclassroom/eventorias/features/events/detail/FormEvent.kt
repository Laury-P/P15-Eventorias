package com.openclassroom.eventorias.features.events.detail

import android.net.Uri


sealed interface FormEvent {
    data class TitleChanged(val title : String) : FormEvent

    data class DescriptionChanged(val description : String) : FormEvent

    data class DateChanged(val date : String) : FormEvent

    data class TimeChanged(val time : String) : FormEvent

    data class AddressChanged (val address : String) : FormEvent

    data class PhotoSelected(val uri : Uri) : FormEvent

    data object OnSaveClicked : FormEvent
}