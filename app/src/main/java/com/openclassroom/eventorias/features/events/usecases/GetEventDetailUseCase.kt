package com.openclassroom.eventorias.features.events.usecases

import com.openclassroom.eventorias.core.domain.repository.EventRepository
import com.openclassroom.eventorias.core.domain.repository.UserRepository
import com.openclassroom.eventorias.features.events.detail.model.DetailEventUiModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetEventDetailUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(eventId: String): Flow<Result<DetailEventUiModel>> {
        return eventRepository.getEventById(eventId)
            .map { event ->
                val safeEvent = event
                    ?: return@map Result.failure(
                        NoSuchElementException("Event $eventId not found")
                    )

                val promoter = userRepository.getCurrentUser(safeEvent.promoterId)
                val promoterURL = promoter?.avatar ?: ""

                Result.success(DetailEventUiModel(event = safeEvent, promoterUrl = promoterURL))
            }
            .catch { exception ->
                emit(Result.failure(exception))
            }
    }
}