package com.openclassroom.eventorias.features.event.eventList

import com.openclassroom.eventorias.features.events.eventList.EventListViewModel
import com.openclassroom.eventorias.features.events.usecases.GetUiEventListUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val useCase: GetUiEventListUseCase = mockk()

    private lateinit var viewModel: EventListViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { useCase() } returns emptyFlow()

        viewModel = EventListViewModel(useCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given viewModel, when interacting with search, then updates searchQuery correctly`() = runTest {

        assertEquals("", viewModel.searchQuery.value)

        viewModel.setSearchQuery("Concert")
        assertEquals("Concert", viewModel.searchQuery.value)

        viewModel.setSearchQuery("")
        assertEquals("", viewModel.searchQuery.value)
    }
}