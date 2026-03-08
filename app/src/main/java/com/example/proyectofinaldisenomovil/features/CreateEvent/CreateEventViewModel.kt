package com.example.proyectofinaldisenomovil.features.CreateEvent

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CreateEventUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val capacity: String = "",
    val images: List<String> = emptyList(),
    val address: String = "",
    val startDate: String = "16 de febrero del 2026",
    val startTime: String = "7:00 pm",
    val endDate: String = "16 de febrero del 2026",
    val endTime: String = "9:00 pm"
)

class CreateEventViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onCategoryChange(newCategory: String) {
        _uiState.update { it.copy(category = newCategory) }
    }

    fun onCapacityChange(newCapacity: String) {
        _uiState.update { it.copy(capacity = newCapacity) }
    }

    fun onAddressChange(newAddress: String) {
        _uiState.update { it.copy(address = newAddress) }
    }

    fun onCreateEvent() {
        // Implementation for creating event
    }
}
