package com.openclassroom.eventorias.features.events.eventList

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassroom.eventorias.core.domain.model.Event
import com.openclassroom.eventorias.core.domain.model.EventCategory
import com.openclassroom.eventorias.core.ui.theme.EventoriasTheme
import com.openclassroom.eventorias.features.events.eventList.model.ListEventUiModel
import com.openclassroom.eventorias.features.events.usecases.GetUiEventListUseCase
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class EventListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val useCase: GetUiEventListUseCase = mockk()
    private lateinit var viewModel: EventListViewModel

    private val fakeNavigator: DestinationsNavigator = mockk(relaxed = true)

    // Nos données de test
    private val testDate = LocalDate.now().plusDays(1)
    private val mockEvents = listOf(
        createMockUiEvent("Concert de Rock", EventCategory.MUSIC, testDate),
        createMockUiEvent("Match de Foot", EventCategory.SPORTS, LocalDate.of(2026, 7, 1))
    )

    @Before
    fun setUp() {
        every { useCase() } returns flowOf(Result.success((mockEvents)))
        viewModel = EventListViewModel(useCase)

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // --- TEST 1 : ÉTAT INITIAL ---
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun initialState_shouldDisplayAllEventsAndNoBadges() {
        composeTestRule.setContent {
            EventoriasTheme {
                Scaffold { innerPadding ->
                    EventListScreen(
                        viewModel = viewModel,
                        navigator = fakeNavigator,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Concert de Rock").fetchSemanticsNodes().isNotEmpty()
        }

        // VÉRIFICATION : Tout le catalogue est affiché par défaut
        composeTestRule.onNodeWithText("Concert de Rock").assertIsDisplayed()
        composeTestRule.onNodeWithText("Match de Foot").assertIsDisplayed()

        // Les badges de filtres actifs ne doivent pas exister puisqu'aucun filtre n'est mis
        composeTestRule.onNodeWithTag("date_filter_chip").assertDoesNotExist()
        composeTestRule.onNodeWithTag("category_filter_chip").assertDoesNotExist()
    }

    // --- TEST 2 : LA RECHERCHE TEXTUELLE + RESET ---
    @Test
    fun searchFunction_shouldFilterListCorrectly_andCloseBouton_shouldResetListCorrectly() {
        // Given
        composeTestRule.setContent {
            EventoriasTheme {
                EventListScreen(viewModel = viewModel, navigator = fakeNavigator)
            }
        }
        composeTestRule.waitForIdle()

        // When
        composeTestRule.onNodeWithTag("search_button").performClick()
        composeTestRule.onNodeWithTag("search_field").performTextInput("Rock")

        // Then : Seul le concert de Rock reste à l'écran
        composeTestRule.waitUntil(timeoutMillis = 1000) {
            composeTestRule
                .onAllNodesWithText("Match de Foot")
                .fetchSemanticsNodes()
                .isEmpty()
        }
        composeTestRule.onNodeWithText("Concert de Rock").assertIsDisplayed()


        // Given: L'utilisateur efface sa recherche
        composeTestRule.onNodeWithTag("close_search_button").performClick()

        // Then : La liste complète revient
        composeTestRule.waitUntil(timeoutMillis = 1000) {
            composeTestRule
                .onAllNodesWithText("Match de Foot")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeTestRule.onNodeWithText("Concert de Rock").assertIsDisplayed()
    }

    @Test
    fun categoryFilter_shouldFilterList_andResetCorrectly_whenFilterIsRemoved() {
        // Given
        composeTestRule.setContent {
            EventoriasTheme {
                EventListScreen(viewModel = viewModel, navigator = fakeNavigator)
            }
        }
        composeTestRule.waitForIdle()

        // When
        composeTestRule.onNodeWithTag("filter_button").performClick()
        composeTestRule.onNodeWithTag("MUSIC_filter_button").performClick()

        // Then : Seul le concert de Rock reste à l'écran
        composeTestRule.waitUntil(timeoutMillis = 1000) {
            composeTestRule
                .onAllNodesWithText("Match de Foot")
                .fetchSemanticsNodes()
                .isEmpty()
        }
        composeTestRule.onNodeWithText("Concert de Rock").assertIsDisplayed()

        // When : on ferme l'écran de filtre
        composeTestRule.onNodeWithTag("filter_button").performClick()

        // Then : Filtre toujours actif et s'affiche au dessus de la liste
        composeTestRule.onNodeWithText("Match de Foot").assertDoesNotExist()
        composeTestRule.onNodeWithTag("category_filter_chip").assertIsDisplayed()

        // When : on supprime le filtre
        composeTestRule.onNodeWithTag("category_filter_chip").performClick()

        // Then : La liste complète revient
        composeTestRule.waitUntil(timeoutMillis = 1000) {
            composeTestRule
                .onAllNodesWithText("Match de Foot")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeTestRule.onNodeWithText("Concert de Rock").assertIsDisplayed()
    }

    @Test
    fun dateFilter_shouldFilterList_andResetCorrectly_whenFilterIsRemoved() {
        // Given
        composeTestRule.setContent {
            EventoriasTheme {
                EventListScreen(viewModel = viewModel, navigator = fakeNavigator)
            }
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Concert de Rock").fetchSemanticsNodes().isNotEmpty()
        }

        // When : on applique une date filtre (pas besoin de tester le datePicker de MaterialDesign)
        composeTestRule.runOnIdle { viewModel.setDateFilter(testDate) }

        // Then : Seul le concert de Rock reste à l'écran
        composeTestRule.waitUntil(timeoutMillis = 1000) {
            composeTestRule
                .onAllNodesWithText("Match de Foot")
                .fetchSemanticsNodes()
                .isEmpty()
        }
        composeTestRule.onNodeWithText("Concert de Rock").assertIsDisplayed()

        // Then : Filtre toujours actif et s'affiche au dessus de la liste
        composeTestRule.onNodeWithText("Match de Foot").assertDoesNotExist()
        composeTestRule.onNodeWithTag("date_filter_chip").assertIsDisplayed()

        // When : on supprime le filtre
        composeTestRule.onNodeWithTag("date_filter_chip").performClick()

        // Then : La liste complète revient
        composeTestRule.waitUntil(timeoutMillis = 1000) {
            composeTestRule
                .onAllNodesWithText("Match de Foot")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeTestRule.onNodeWithText("Concert de Rock").assertIsDisplayed()
    }

    private fun createMockUiEvent(
        title: String,
        category: EventCategory,
        date: LocalDate
    ): ListEventUiModel {
        val event = mockk<Event>()
        every { event.title } returns title
        every { event.category } returns category
        every { event.dateTime } returns date.atStartOfDay()
        every { event.photoUrl} returns ""


        val uiEvent = mockk<ListEventUiModel>()
        every { uiEvent.event } returns event
        every { uiEvent.promoterAvatarUrl} returns ""
        return uiEvent
    }
}