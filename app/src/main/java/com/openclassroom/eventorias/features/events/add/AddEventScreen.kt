package com.openclassroom.eventorias.features.events.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassroom.eventorias.R
import com.openclassroom.eventorias.core.ui.theme.EventoriasTheme
import com.openclassroom.eventorias.features.events.add.component.CategorySelector
import com.openclassroom.eventorias.features.events.add.component.DateTimeSelector
import com.openclassroom.eventorias.features.events.detail.FormEvent
import com.openclassroom.eventorias.features.events.detail.IsPublishing
import com.openclassroom.eventorias.features.events.eventList.component.LoadingComponent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.EventListScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun AddEventScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: AddEventViewModel = hiltViewModel()
) {
    val newEvent by viewModel.newEvent.collectAsStateWithLifecycle()
    val isPublishing by viewModel.isPublishing.collectAsStateWithLifecycle()

    LaunchedEffect(isPublishing) {
        if (isPublishing is IsPublishing.Published){
            navigator.navigate(EventListScreenDestination)
        }
    }

    //TODO Améliorer gestion d'erreur

    if(isPublishing is IsPublishing.Publishing) LoadingComponent()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_screen_title),
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
                },
            )
        }
    ) { innerPadding ->

        val dims = EventoriasTheme.dimensions
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = dims.padding24),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(dims.padding24)) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newEvent.title,
                    onValueChange = { viewModel.onAction(FormEvent.TitleChanged(it)) },
                    label = { Text(stringResource(R.string.title_label)) },
                    placeholder = { Text(stringResource(R.string.title_placeholder)) },
                    shape = MaterialTheme.shapes.small,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newEvent.description,
                    onValueChange = { viewModel.onAction(FormEvent.DescriptionChanged(it)) },
                    label = { Text(stringResource(R.string.description_label)) },
                    placeholder = { Text(stringResource(R.string.description_placeholder)) },
                    shape = MaterialTheme.shapes.small,
                    maxLines = 5
                )
                DateTimeSelector(
                    date = newEvent.date,
                    time = newEvent.time,
                    onDateSelected = { viewModel.onAction(FormEvent.DateChanged(it)) },
                    onTimeSelected = { viewModel.onAction(FormEvent.TimeChanged(it)) }
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newEvent.address,
                    onValueChange = { viewModel.onAction(FormEvent.AddressChanged(it)) },
                    label = { Text(stringResource(R.string.address_label)) },
                    placeholder = { Text(stringResource(R.string.address_placeholder)) },
                    shape = MaterialTheme.shapes.small,
                    maxLines = 2,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                )

                CategorySelector(
                    selectedCategory = newEvent.category,
                    onCategorySelected = { viewModel.onAction(FormEvent.CategoryChanged(it)) },
                )

                Row(
                    modifier = Modifier
                        .padding(top = dims.padding48)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton(
                        onClick = {},
                        colors = IconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            disabledContentColor = MaterialTheme.colorScheme.onTertiary,
                            disabledContainerColor = MaterialTheme.colorScheme.tertiary
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = stringResource(R.string.take_photo_description),
                        )
                    }
                    Spacer(modifier = Modifier.width(dims.padding16))
                    IconButton(
                        onClick = {},
                        colors = IconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.onTertiary,
                            disabledContainerColor = MaterialTheme.colorScheme.tertiary
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = stringResource(R.string.select_from_gallery_description)
                        )
                    }
                }
            }


            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { viewModel.onAction(FormEvent.OnSaveClicked) },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary
                ),
                shape = MaterialTheme.shapes.small,
                enabled = isPublishing != IsPublishing.Publishing
            ) {
                Text(
                    text =
                        if (isPublishing is IsPublishing.Error) {
                            stringResource(R.string.retry_button)
                        } else stringResource(R.string.add_event_button),
                )
            }

        }

    }
}

