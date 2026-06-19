package com.openclassroom.eventorias.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassroom.eventorias.core.domain.model.User
import com.openclassroom.eventorias.features.profile.usecases.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(getUserInfoUseCase: GetUserInfoUseCase) :
    ViewModel() {

    val uiState: StateFlow<UiState> = getUserInfoUseCase()
        .map { result ->
            result.fold(
                onSuccess = { user -> UiState.Success(user) },
                onFailure = { exception -> UiState.Error(exception.message ?: "Unknown error")}
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Idle
        )

}

sealed interface UiState {
    data object Idle : UiState
    data class Success(val user: User) : UiState
    data class Error(val error: String) : UiState
}