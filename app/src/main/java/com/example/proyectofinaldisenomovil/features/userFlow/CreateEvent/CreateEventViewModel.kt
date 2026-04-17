package com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.google.firebase.Timestamp
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

sealed class CreateEventResult {
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
    val dateError: String = "",
    val isFormValid: Boolean = false
)

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    private val _createResult = MutableStateFlow<CreateEventResult>(CreateEventResult.Idle)
    val createResult: StateFlow<CreateEventResult> = _createResult.asStateFlow()

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

    fun onTitleChange(newTitle: String) {
        updateState { 
            it.copy(
                title = newTitle,
                titleError = if (newTitle.isNotEmpty() && newTitle.length < 5) "El título debe tener al menos 5 caracteres" else ""
            ) 
        }
    }

    fun onDescriptionChange(newDescription: String) {
        updateState { 
            it.copy(
                description = newDescription,
                descriptionError = if (newDescription.isNotEmpty() && newDescription.length < 20) "La descripción debe tener al menos 20 caracteres" else ""
            ) 
        }
    }

    fun onCategoryChange(newCategory: EventCategory) {
        updateState { it.copy(category = newCategory) }
    }

    fun onCapacityChange(newCapacity: String) {
        updateState { it.copy(capacity = newCapacity.filter { c -> c.isDigit() }) }
    }

    fun onAddressChange(newAddress: String) {
        updateState { 
            it.copy(
                address = newAddress,
                addressError = if (newAddress.isNotEmpty() && newAddress.length < 10) "Dirección muy corta" else ""
            ) 
        }
    }

    fun addImage(uri: Uri) {
        if (_uiState.value.images.size < 5) {
            updateState { it.copy(images = it.images + uri) }
        }
    }

    fun removeImage(uri: Uri) {
        updateState { it.copy(images = it.images - uri) }
    }

    fun onStartDateChange(millis: Long?) {
        millis?.let {
            val dateString = dateFormatter.format(Date(it))
            val timeString = timeFormatter.format(Date(it))
            updateState { state -> 
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
            updateState { state -> 
                state.copy(
                    endDate = dateString,
                    endTime = timeString,
                    dateError = ""
                ) 
            }
        }
    }

    fun createEvent() {
        val state = _uiState.value
        
        if (!state.isFormValid) {
            val missingFields = mutableListOf<String>()
            if (state.title.length < 1) missingFields.add("Título (mín. 1)")
            if (state.description.length < 1) missingFields.add("Descripción (mín. 1)")
            if (state.address.length < 1) missingFields.add("Dirección (mín. 1)")
            if (state.startDate.isEmpty()) missingFields.add("Fecha Inicio")

            _createResult.value = CreateEventResult.Error("Falta completar: ${missingFields.joinToString(", ")}")
            return
        }

        viewModelScope.launch {
            _createResult.value = CreateEventResult.Loading
            val capacity = state.capacity.replace(".", "").toIntOrNull()

            Log.i("Crear evento" , "Iniciando creación")
            try {
                eventRepository.createEvent(
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
                _createResult.value = CreateEventResult.Error("Error: ${e.message}")
            }
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
