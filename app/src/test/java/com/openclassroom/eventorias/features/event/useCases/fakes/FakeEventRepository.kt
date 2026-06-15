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

    override fun getEventById(id: String): Flow<Event?> {
        return events.map { list -> list.find { it.id == id } }
    }

    override fun getParticipantsList(eventId: String): Flow<List<String>> {
        return participants.map { map -> map[eventId] ?: emptyList() }
    }

    var lastSavedStatus: Boolean? = null
    var lastSavedEventId: String? = null
    var lastSavedUserId: String? = null
    var shouldReturnFailure = false

    override suspend fun setParticipationStatus(
        newStatus: Boolean,
        userId: String,
        eventId: String
    ): Result<Unit> {
        if (shouldReturnFailure) {
            return Result.failure(Exception("Firebase Error"))
        }
        lastSavedStatus = newStatus
        lastSavedUserId = userId
        lastSavedEventId = eventId

        return Result.success(Unit)
    }
}
