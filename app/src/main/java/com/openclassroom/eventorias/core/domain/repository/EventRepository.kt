package com.openclassroom.eventorias.core.domain.repository

import com.openclassroom.eventorias.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getListEvent() : Flow<List<Event>>
}