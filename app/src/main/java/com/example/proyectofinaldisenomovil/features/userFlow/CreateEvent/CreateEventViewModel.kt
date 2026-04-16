package com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
sealed class CreateEventResult @Inject constructor(

) {
    data object Idle : CreateEventResult()
    data object Loading : CreateEventResult()
    data object Success : CreateEventResult()
    data class Error(val message: String) : CreateEventResult()
}

data class CreateEventUiState(
    val title: String = "",
    val description: String = "",
    val category: EventCategory = EventCategory.SOCIAL,
    val capacity: String = "",
    val images: List<Uri> = emptyList(),
    val address: String = "",
    val startDate: String = "",
    val startTime: String = "",
    val endDate: String = "",
    val endTime: String = "",
    val titleError: String = "",
    val descriptionError: String = "",
    val addressError: String = "",
    val dateError: String = ""
)

class CreateEventViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    private val _createResult = MutableStateFlow<CreateEventResult>(CreateEventResult.Idle)
    val createResult: StateFlow<CreateEventResult> = _createResult.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd 'de' MMMM 'del' yyyy", Locale("es", "CO"))
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale("es", "CO"))

    fun onTitleChange(newTitle: String) {
        _uiState.update { 
            it.copy(
                title = newTitle,
                titleError = if (newTitle.isNotEmpty() && newTitle.length < 5) "El título debe tener al menos 5 caracteres" else ""
            ) 
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { 
            it.copy(
                description = newDescription,
                descriptionError = if (newDescription.isNotEmpty() && newDescription.length < 20) "La descripción debe tener al menos 20 caracteres" else ""
            ) 
        }
    }

    fun onCategoryChange(newCategory: EventCategory) {
        _uiState.update { it.copy(category = newCategory) }
    }

    fun onCapacityChange(newCapacity: String) {
        _uiState.update { it.copy(capacity = newCapacity.filter { c -> c.isDigit() }) }
    }

    fun onAddressChange(newAddress: String) {
        _uiState.update { 
            it.copy(
                address = newAddress,
                addressError = if (newAddress.isNotEmpty() && newAddress.length < 10) "Dirección muy corta" else ""
            ) 
        }
    }

    fun addImage(uri: Uri) {
        if (_uiState.value.images.size < 5) {
            _uiState.update { it.copy(images = it.images + uri) }
        }
    }

    fun removeImage(uri: Uri) {
        _uiState.update { it.copy(images = it.images - uri) }
    }

    fun onStartDateChange(millis: Long?) {
        millis?.let {
            val dateString = dateFormatter.format(Date(it))
            val timeString = timeFormatter.format(Date(it))
            _uiState.update { state -> 
                state.copy(
                    startDate = dateString,
                    startTime = timeString,
                    dateError = ""
                ) 
            }
        }
    }

    fun onEndDateChange(millis: Long?) {
        millis?.let {
            val dateString = dateFormatter.format(Date(it))
            val timeString = timeFormatter.format(Date(it))
            _uiState.update { state -> 
                state.copy(
                    endDate = dateString,
                    endTime = timeString,
                    dateError = ""
                ) 
            }
        }
    }

    fun validateForm(): Boolean {
        val state = _uiState.value
        var isValid = true

        if (state.title.length < 5) {
            _uiState.update { it.copy(titleError = "El título debe tener al menos 5 caracteres") }
            isValid = false
        }

        if (state.description.length < 20) {
            _uiState.update { it.copy(descriptionError = "La descripción debe tener al menos 20 caracteres") }
            isValid = false
        }

        if (state.address.length < 10) {
            _uiState.update { it.copy(addressError = "Dirección muy corta") }
            isValid = false
        }

        if (state.startDate.isEmpty() || state.endDate.isEmpty()) {
            _uiState.update { it.copy(dateError = "Debes seleccionar fecha de inicio y fin") }
            isValid = false
        }

        return isValid
    }

    fun createEvent() {
        if (!validateForm()) {
            _createResult.value = CreateEventResult.Error("Por favor completa todos los campos")
            return
        }

        _createResult.value = CreateEventResult.Loading

        val state = _uiState.value
        val capacity = state.capacity.toIntOrNull()

        try {
            val event = MockDataRepository.createEvent(
                title = state.title.trim(),
                description = state.description.trim(),
                category = state.category,
                address = state.address.trim(),
                imageUrls = if (state.images.isNotEmpty()) {
                    state.images.map { it.toString() }
                } else {
                    listOf("https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800")
                },
                startDate = Timestamp(Date()),
                endDate = Timestamp(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)),
                maxAttendees = capacity
            )

            _createResult.value = CreateEventResult.Success
        } catch (e: Exception) {
            _createResult.value = CreateEventResult.Error("Error al crear el evento: ${e.message}")
        }
    }

    fun resetResult() {
        _createResult.value = CreateEventResult.Idle
    }

    fun clearForm() {
        _uiState.value = CreateEventUiState()
        _createResult.value = CreateEventResult.Idle
    }
}
