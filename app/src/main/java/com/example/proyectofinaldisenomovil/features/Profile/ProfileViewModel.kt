package com.example.proyectofinaldisenomovil.features.Profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
    val rating: Double = 4.7
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onLogout() {
        // Implement logout logic
    }
}