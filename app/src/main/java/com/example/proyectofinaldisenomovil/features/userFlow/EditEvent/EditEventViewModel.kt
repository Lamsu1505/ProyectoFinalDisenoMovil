package com.example.proyectofinaldisenomovil.features.userFlow.EditEvent

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent.CreateEventResult
import com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent.CreateEventUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    private val _editResult = MutableStateFlow<CreateEventResult>(CreateEventResult.Idle)
    val editResult: StateFlow<CreateEventResult> = _editResult.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd 'de' MMMM 'del' yyyy", Locale("es", "CO"))
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale("es", "CO"))

    private fun checkFormValidity(state: CreateEventUiState): Boolean {
        return state.title.length >= 1 &&
                state.description.length >= 1 &&
                state.address.length >= 1 &&
                state.startDate.isNotEmpty()
    }

    private fun updateState(update: (CreateEventUiState) -> CreateEventUiState) {
        _uiState.update { currentState ->
            val newState = update(currentState)
            newState.copy(isFormValid = checkFormValidity(newState))
        }
    }

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            val event = eventRepository.getEventById(eventId)
            event?.let { e ->
                updateState { state ->
                    state.copy(
                        idEvent = e.id,
                        title = e.title,
                        description = e.description,
                        category = e.category,
                        capacity = e.maxAttendees?.toString() ?: "",
                        address = e.address,
                        startDate = e.startDate?.let { dateFormatter.format(it.toDate()) } ?: "",
                        startTime = e.startDate?.let { timeFormatter.format(it.toDate()) } ?: "",
                        endDate = e.endDate?.let { dateFormatter.format(it.toDate()) } ?: "",
                        endTime = e.endDate?.let { timeFormatter.format(it.toDate()) } ?: "",
                    )
                }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        updateState { it.copy(title = newTitle) }
    }

    fun onDescriptionChange(newDescription: String) {
        updateState { it.copy(description = newDescription) }
    }

    fun onCategoryChange(newCategory: EventCategory) {
        updateState { it.copy(category = newCategory) }
    }

    fun onCapacityChange(newCapacity: String) {
        updateState { it.copy(capacity = newCapacity.filter { it.isDigit() }) }
    }

    fun onAddressChange(newAddress: String) {
        updateState { it.copy(address = newAddress) }
    }

    fun onStartDateChange(millis: Long?) {
        millis?.let {
            val dateString = dateFormatter.format(Date(it))
            val timeString = timeFormatter.format(Date(it))
            updateState { it.copy(startDate = dateString, startTime = timeString) }
        }
    }

    fun onEndDateChange(millis: Long?) {
        millis?.let {
            val dateString = dateFormatter.format(Date(it))
            val timeString = timeFormatter.format(Date(it))
            updateState { it.copy(endDate = dateString, endTime = timeString) }
        }
    }

    fun addImage(uri: Uri) {
        updateState { it.copy(images = it.images + uri) }
    }

    fun removeImage(uri: Uri) {
        updateState { it.copy(images = it.images - uri) }
    }

    fun saveChanges() {
        val state = _uiState.value
        val eventId = state.idEvent ?: return
        Log.i("Editar evento", "Guardando cambios del evento " + eventId)

        viewModelScope.launch {
            _editResult.value = CreateEventResult.Loading
            try {
                // Buscamos el original para no perder metadatos (autor, asistentes actual, etc)
                val originalEvent = eventRepository.getEventById(eventId)
                
                if (originalEvent != null) {
                    val updatedEvent = originalEvent.copy(
                        title = state.title.trim(),
                        description = state.description.trim(),
                        category = state.category,
                        address = state.address.trim(),
                        maxAttendees = state.capacity.toIntOrNull()
                        // Nota: images y dates se pueden añadir si el repo lo soporta
                    )
                    
                    eventRepository.editEvent(eventId, updatedEvent)
                    _editResult.value = CreateEventResult.Success
                } else {
                    _editResult.value = CreateEventResult.Error("No se encontró el evento original")
                }
            } catch (e: Exception) {
                _editResult.value = CreateEventResult.Error("Error: ${e.message}")
            }
        }
    }

    fun resetResult() {
        _editResult.value = CreateEventResult.Idle
    }
}
