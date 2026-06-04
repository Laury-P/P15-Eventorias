package com.openclassroom.eventorias.features.events.usecases

import com.openclassroom.eventorias.core.data.repository.FirebaseUserRepository
import com.openclassroom.eventorias.core.domain.repository.EventRepository
import com.openclassroom.eventorias.features.events.eventList.model.ListEventUiModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUiEventListUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: FirebaseUserRepository
) {
    operator fun invoke() : Flow<List<ListEventUiModel>> {
        return eventRepository.getListEvent().map{ eventList ->
            eventList.map { event ->
                val promoter = userRepository.getCurrentUser(event.promoterId)
                val promoterURL = promoter?.avatar ?: ""
                ListEventUiModel(event = event, promoterAvatarUrl = promoterURL)
            }
        }
    }

}