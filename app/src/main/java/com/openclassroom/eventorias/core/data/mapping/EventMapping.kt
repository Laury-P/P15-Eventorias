package com.openclassroom.eventorias.core.data.mapping

import com.openclassroom.eventorias.core.data.model.EventDto
import com.openclassroom.eventorias.core.domain.model.Event
import java.time.LocalDateTime

fun EventDto.toDomain() : Event {
    return Event(
        id = this.id,
        title = this.title,
        description = this.description,
        dateTime = LocalDateTime.parse(this.dateTime),
        location = this.location,
        photoUrl = this.photoUrl,
        promoterId = this.promoterId
    )
}

fun Event.toDto() : EventDto {
    return EventDto(
        id = this.id,
        title = this.title,
        description = this.description,
        dateTime = this.dateTime.toString(),
        location = this.location,
        photoUrl = this.photoUrl,
        promoterId = this.promoterId
    )
}