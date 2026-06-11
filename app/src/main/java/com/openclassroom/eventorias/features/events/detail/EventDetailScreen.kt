package com.openclassroom.eventorias.features.events.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.openclassroom.eventorias.R
import com.openclassroom.eventorias.core.ui.theme.EventoriasTheme
import com.openclassroom.eventorias.features.events.detail.component.AddressItem
import com.openclassroom.eventorias.features.events.detail.component.DateTimeItem
import com.openclassroom.eventorias.features.events.detail.model.DetailEventUiState
import com.openclassroom.eventorias.features.events.eventList.component.LoadingComponent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.EventListScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun EventDetailScreen(
    eventId: String,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val detailState by viewModel.uiState.collectAsStateWithLifecycle()
    val dims = EventoriasTheme.dimensions

    when (val currentState = detailState) {
        is DetailEventUiState.Loading -> LoadingComponent()
        is DetailEventUiState.Error -> {
            LaunchedEffect(currentState) {
                delay(2500)
                navigator.navigate(EventListScreenDestination)
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.event_not_found),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground)
            }
        }

        is DetailEventUiState.Success -> {
            val event = currentState.eventDetail.event
            val promoterAvatar = currentState.eventDetail.promoterUrl

            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        windowInsets = WindowInsets(0, 0, 0, 0),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground,
                            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        navigationIcon = {
                            IconButton(
                                onClick = { navigator.popBackStack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back_button),
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = dims.padding24)
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(dims.padding24)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.medium),
                        model = event.photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DateTimeItem(dateTime = event.dateTime)
                        AsyncImage(
                            modifier = Modifier
                                .size(dims.avatarDetail)
                                .clip(CircleShape),
                            model = promoterAvatar,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }

                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )

                    AddressItem(
                        modifier = Modifier.padding(top = dims.padding24),
                        address = event.location
                    )
                }
            }
        }

    }


}
