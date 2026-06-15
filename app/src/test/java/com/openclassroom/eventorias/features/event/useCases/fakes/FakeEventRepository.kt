package com.openclassroom.eventorias.features.event.useCases.fakes

import com.openclassroom.eventorias.core.domain.model.Event
import com.openclassroom.eventorias.core.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map


class FakeEventRepository : EventRepository {

    private val events = MutableStateFlow<List<Event>>(emptyList())

    private val participants = MutableStateFlow<Map<String, List<String>>>(emptyMap())

    fun emitEvents(newEvents: List<Event>) {
        events.value = newEvents
    }

    fun emitParticipants(eventId: String, userIds: List<String>) {
        participants.value += (eventId to userIds)
    }

    override fun getListEvent(): Flow<List<Event>> {
        return events
    }

    override fun getEventById(eventId: String): Flow<Event?> {
        return events.map { list -> list.find { it.id == eventId } }
    }

    override fun getParticipantsList(eventId: String): Flow<List<String>> {
        return participants.map { map -> map[eventId] ?: emptyList() }
    }

    override suspend fun setParticipationStatus(
        newStatus: Boolean,
        userId: String,
        eventId: String
    ): Result<Unit> = Result.success(Unit)
}
