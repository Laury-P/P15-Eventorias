package com.openclassroom.eventorias.features.events.usecases

import com.openclassroom.eventorias.core.domain.repository.AuthRepository
import com.openclassroom.eventorias.core.domain.repository.EventRepository
import jakarta.inject.Inject

class SetUserParticipationUseCase @Inject constructor(private val authRepository: AuthRepository, private val eventRepository: EventRepository) {
    suspend operator fun invoke(newStatus : Boolean, eventId : String) {
        val currentUserId = authRepository.getUserId()
        currentUserId?.let {
            eventRepository.setParticipationStatus(newStatus, currentUserId, eventId)
        }
    }
}