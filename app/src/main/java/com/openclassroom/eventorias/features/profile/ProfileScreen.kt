package com.openclassroom.eventorias.features.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.openclassroom.eventorias.R
import com.openclassroom.eventorias.core.domain.model.User
import com.openclassroom.eventorias.core.ui.theme.EventoriasTheme
import com.openclassroom.eventorias.features.events.eventList.component.LoadingComponent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.EventListScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userState by viewModel.uiState.collectAsStateWithLifecycle()

    val dims = EventoriasTheme.dimensions

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_screen_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        AsyncImage(
                            model = (userState as UiState.Success).user.avatar,
                            modifier = Modifier
                                .size(dims.avatarDetail)
                                .clip(CircleShape),
                            contentDescription = stringResource(R.string.avatar_description),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (userState) {
            is UiState.Success -> {
                val user = (userState as UiState.Success).user
                ProfileContent(
                    modifier = modifier.padding(innerPadding),
                    user = user,
                    onLogoutClick = {}, //TODO,
                    notificationStatus = true,
                    onSwitchClicked = {}
                )
            }
            is UiState.Idle -> LoadingComponent()
            is UiState.Error -> {
                // TODO Gerer erreur lors de la deconnexions != de erreur profil ou erreur modifications
                LaunchedEffect(userState) {
                    delay(2500)
                    navigator.navigate(EventListScreenDestination)
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.profile_error),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    user: User,
    notificationStatus: Boolean,
    onSwitchClicked: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val dims = EventoriasTheme.dimensions
    Column(
        modifier = modifier
            .padding(horizontal = dims.padding24),
        verticalArrangement = Arrangement.spacedBy(dims.padding24)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "${user.firstname} ${user.lastname}",
            onValueChange = {},
            label = { Text(stringResource(R.string.name_label)) },
            shape = MaterialTheme.shapes.small,
            singleLine = true,
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = user.email,
            onValueChange = {},
            label = { Text(stringResource(R.string.email_label)) },
            shape = MaterialTheme.shapes.small,
            singleLine = true
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dims.padding12),
            modifier = Modifier
                .testTag("notification_switch")
                .toggleable(
                    value = notificationStatus,
                    onValueChange = { onSwitchClicked() },
                    role = Role.Switch,
                )
                .semantics(mergeDescendants = true) { }
                .padding(dims.padding8)
        ) {
            Switch(
                checked = notificationStatus,
                onCheckedChange = null,
                modifier = Modifier.clearAndSetSemantics {}
            )
            Text(
                text = stringResource(R.string.notification_switch_label),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
            )
        }

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("logout_button"),
            onClick = { onLogoutClick() },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onTertiary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary
            ),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(
                text = stringResource(R.string.logout_button),
                style = MaterialTheme.typography.titleMedium
            )
        }

    }

}


@Preview
@Composable
fun ProfileContentPreview() {
    EventoriasTheme(
        {
            ProfileContent(
                user = User(
                    id = "123",
                    email = "adrien.chardon@email.com",
                    firstname = "Adrien",
                    lastname = "Chardon",
                    avatar = "https://firebasestorage.googleapis.com/v0/b/eventorias-15e42.firebasestorage.app/o/52d7360517a4a8f4c1bf360e6a103532ef0b53b8.png?alt=media&token=ba39425d-7035-4184-930c-4082849e4942",
                ),
                onSwitchClicked = { },
                onLogoutClick = {},
                notificationStatus = true,
            )
        }
    )
}