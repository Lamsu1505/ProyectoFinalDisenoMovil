package com.example.proyectofinaldisenomovil.features.userFlow.Profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.data.local.SettingsDataStore
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.User.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String?,
    val location: String?,
    val level: Int?,
    val levelName: String?,
    val points: Int?,
    val pointsToNextLevel: Int?,
    val activeEvents: Int?,
    val completedEvents: Int?,
    val pendingEvents: Int?,
    val rating: Double?,
    val isLoading: Boolean?,
    val successMessage: String? ,
    val errorMessage: String?
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val settingsDataStore: SettingsDataStore,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(
        name = null,
        location = null,
        level = null,
        levelName = null,
        points = null,
        pointsToNextLevel = null,
        activeEvents = null,
        completedEvents = null,
        pendingEvents = null,
        rating = null, // Empezamos en null
        isLoading = true,
        successMessage = null,
        errorMessage = null
    ))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    val currentLanguage: StateFlow<String> = settingsDataStore.languageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsDataStore.DEFAULT_LANGUAGE
        )

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            delay(500)

            val loggedInUser = MockDataRepository.getLoggedInUser()
            val user = loggedInUser?.let { userRepository.getUserById(it.uid) }
            if (user != null) {
                _uiState.value = _uiState.value.copy(
                    name = user.fullName,
                    location = user.city,
                    level = 1,
                    levelName = user.level.toString(),
                    points = user.reputationPoints,
                    pointsToNextLevel = user.pointsToNextLevel() ?: 0,
                    activeEvents = 10,
                    completedEvents = 13,
                    pendingEvents = 20,
                    rating = user.rating,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = resourceProvider.getString(R.string.profile_error_load)
                )
            }
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsDataStore.setLanguage(languageCode)
            _uiState.value = _uiState.value.copy(
                successMessage = resourceProvider.getString(R.string.language_change_success)
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }

    fun getLanguageDisplayName(languageCode: String): String {
        return when (languageCode) {
            "es" -> resourceProvider.getString(R.string.language_spanish)
            "en" -> resourceProvider.getString(R.string.language_english)
            else -> languageCode
        }
    }
}
