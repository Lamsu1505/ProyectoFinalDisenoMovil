package com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CreateEventUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val capacity: String = "",
    val images: List<Uri> = emptyList(),
    val address: String = "",
    val startDate: String = "16 feb del 2026",
    val startTime: String = "7:00 pm",
    val endDate: String = "19 feb del 2026",
    val endTime: String = "9:00 pm"
)

class CreateEventViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd 'de' MMMM 'del' yyyy", Locale("es", "CO"))

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

    fun addImage(uri: Uri) {
        _uiState.update { it.copy(images = it.images + uri) }
    }

    fun removeImage(uri: Uri) {
        _uiState.update { it.copy(images = it.images - uri) }
    }

    fun onStartDateChange(millis: Long?) {
        millis?.let {
            val dateString = dateFormatter.format(Date(it))
            _uiState.update { state -> state.copy(startDate = dateString) }
        }
    }

    fun onEndDateChange(millis: Long?) {
        millis?.let {
            val dateString = dateFormatter.format(Date(it))
            _uiState.update { state -> state.copy(endDate = dateString) }
        }
    }

    fun onCreateEvent() {
        // Implementation for creating event
    }
}
