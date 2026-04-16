package com.example.proyectofinaldisenomovil.features.userFlow.EditProfile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class EditProfileUiState(
    val name: String = "Andres Felipe",
    val lastName: String = "Zuñiga Zuluaga",
    val email: String = "perezmartinezandres0@gmail.com",
    val location: String = "Armenia, Quindio",
    val joinDate: String = "10 de febrero del 2026",
    val passwordPlaceholder: String = "************************"
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onLastNameChange(newLastName: String) {
        _uiState.update { it.copy(lastName = newLastName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onSave() {
        // Handle save logic
    }

    fun onDeleteAccount() {
        // Handle delete account logic
    }
}