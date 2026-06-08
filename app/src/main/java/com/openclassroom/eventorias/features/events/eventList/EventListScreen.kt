package com.openclassroom.eventorias.features.events.eventList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassroom.eventorias.R
import com.openclassroom.eventorias.features.events.eventList.component.ErrorComponent
import com.openclassroom.eventorias.features.events.eventList.component.EventItem
import com.openclassroom.eventorias.features.events.eventList.component.LoadingComponent
import com.openclassroom.eventorias.features.events.eventList.model.ListEventUiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun EventListScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: EventListViewModel = hiltViewModel()
) {
    val listEventState by viewModel.uiState.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var isSearchBarActive by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(isSearchBarActive) {
        if (isSearchBarActive) focusRequester.requestFocus()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    // When in active search mode, a text field, and when no active search, the title of the screen/
                    if (isSearchBarActive) {
                        CustomSearchBar(
                            searchQuery = searchQuery,
                            onQueryChanged = { viewModel.setSearchQuery(it) },
                            focusRequester = focusRequester
                        )
                    } else Text(stringResource(R.string.event_list_screen_title))
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                // Icon only display in active search mode to close the search bar
                navigationIcon = {
                    if (isSearchBarActive) {
                        IconButton(onClick = {
                            viewModel.setSearchQuery("")
                            isSearchBarActive = false
                        }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.close_searchbar_description)
                            )
                        }
                    }
                },
                actions = {
                    // When the search isn't active the buttons are the search button and the filter button
                    if (!isSearchBarActive) {
                        IconButton(
                            onClick = { isSearchBarActive = true },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_button_description)
                            )
                        }

                        IconButton(
                            onClick = { TODO() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = stringResource(R.string.sort_button_description)
                            )
                        }
                    } else {
                        // When the search is active, the button delete the active query
                        IconButton(
                            onClick = {
                                if (searchQuery.isNotEmpty()) viewModel.setSearchQuery("")
                                else isSearchBarActive = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = stringResource(R.string.delete_search_description)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { TODO() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_fab_description)
                )
            }
        },
    ) { innerPadding ->
        when (val currentState = listEventState) {
            is ListEventUiState.Loading -> LoadingComponent()
            is ListEventUiState.Error -> ErrorComponent(onRetryClick = { viewModel.retry() })
            is ListEventUiState.Success -> {
                val list = currentState.listEvent
                if (list.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.empty_list),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    LazyColumn(modifier.padding(innerPadding)) {
                        items(list) { event ->
                            EventItem(uiEvent = event, onEventClick = { TODO() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomSearchBar(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    BasicTextField(
        value = searchQuery,
        onValueChange = { onQueryChanged(it) },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusRequester.freeFocus()
            }
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        decorationBox = { innerTextField ->
            if (searchQuery.isEmpty()) {
                Text(
                    text = stringResource(R.string.search_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
            innerTextField()
        }
    )
}

