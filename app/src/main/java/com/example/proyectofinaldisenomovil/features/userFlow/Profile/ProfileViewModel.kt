package com.example.proyectofinaldisenomovil.features.userFlow.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.data.local.SettingsDataStore
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
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
    val name: String = "Andrés Felipe Zuñiga",
    val location: String = "Armenia, Quindio",
    val level: Int = 2,
    val levelName: String = "Organizador",
    val points: Int = 1820,
    val pointsToNextLevel: Int = 680,
    val activeEvents: Int = 2,
    val completedEvents: Int = 5,
    val pendingEvents: Int = 4,
    val rating: Double = 4.7,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

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

            val user = MockDataRepository.getLoggedInUser()
            if (user != null) {
                _uiState.value = _uiState.value.copy(
                    name = user.fullName,
                    location = user.city,
                    isLoading = false,
                    successMessage = resourceProvider.getString(R.string.profile_success_load)
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
