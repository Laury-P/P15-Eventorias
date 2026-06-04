package com.openclassroom.eventorias.features.event.useCases

import com.openclassroom.eventorias.core.data.repository.FirebaseUserRepository
import com.openclassroom.eventorias.core.domain.model.Event
import com.openclassroom.eventorias.core.domain.model.User
import com.openclassroom.eventorias.core.domain.repository.EventRepository
import com.openclassroom.eventorias.features.events.usecases.GetUiEventListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetUiEventListUseCaseTest {

    private val eventRepository: EventRepository = mockk()
    private val userRepository: FirebaseUserRepository = mockk()

    private val useCase = GetUiEventListUseCase(eventRepository, userRepository)

    @Test
    fun `given events exist, when invoke, then maps with correct promoter avatars`() = runTest {
        // GIVEN :
        val fakeEvent = Event(id = "event_1", title = "Concert", promoterId = "promoter_A")
        val fakeUser = User(id = "promoter_A", avatar = "https://url_de_l_avatar.png")

        every { eventRepository.getListEvent() } returns flowOf(listOf(fakeEvent))

        coEvery { userRepository.getCurrentUser("promoter_A") } returns fakeUser

        // WHEN :
        val resultList = useCase().first()

        // THEN :
        assertEquals(1, resultList.size)

        val uiModel = resultList.first()
        assertEquals("Concert", uiModel.event.title)
        assertEquals("https://url_de_l_avatar.png", uiModel.promoterAvatarUrl)
    }
}