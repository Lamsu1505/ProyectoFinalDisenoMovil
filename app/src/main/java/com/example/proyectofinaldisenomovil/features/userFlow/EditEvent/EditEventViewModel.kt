package com.example.proyectofinaldisenomovil.features.userFlow.EditEvent

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.Remote.CloudinaryRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent.CreateEventResult
import com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent.CreateEventUiState
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.R
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

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val cloudinaryRepository: CloudinaryRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    private val _editResult = MutableStateFlow<CreateEventResult>(CreateEventResult.Idle)
    val editResult: StateFlow<CreateEventResult> = _editResult.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd 'de' MMMM 'del' yyyy", Locale("es", "CO"))
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale("es", "CO"))

    private fun checkFormValidity(state: CreateEventUiState): Boolean {
        return state.title.isNotBlank() &&
                state.description.isNotBlank() &&
                state.address.isNotBlank() &&
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
                        pointerAddres = Location(e.latitude, e.longitude),
                        startDate = e.startDate?.let { dateFormatter.format(it.toDate()) } ?: "",
                        startTime = e.startDate?.let { timeFormatter.format(it.toDate()) } ?: "",
                        endDate = e.endDate?.let { dateFormatter.format(it.toDate()) } ?: "",
                        endTime = e.endDate?.let { timeFormatter.format(it.toDate()) } ?: "",
                        images = e.imageUrls.map { Uri.parse(it) }
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

    fun onPointerAddressChange(newAddress: Location) {
        updateState {
            it.copy(
                pointerAddres = newAddress,
            )
        }
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

        viewModelScope.launch {
            _editResult.value = CreateEventResult.Loading
            try {
                val originalEvent = eventRepository.getEventById(eventId) ?: throw Exception(resourceProvider.getString(R.string.moderator_event_not_found))

                val uploadedImageUrls = if (state.images.isNotEmpty()) {
                    state.images.map { imageUri ->
                        val scheme = imageUri.scheme
                        if (scheme != null && (scheme.equals("http", true) || scheme.equals("https", true))) {
                            imageUri.toString()
                        } else {
                            cloudinaryRepository.uploadImage(imageUri)
                        }
                    }
                } else {
                    listOf("https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800")
                }

                val updatedEvent = originalEvent.copy(
                    title = state.title.trim(),
                    description = state.description.trim(),
                    category = state.category,
                    address = state.address.trim(),
                    latitude = state.pointerAddres.latitude,
                    longitude = state.pointerAddres.longitude,
                    maxAttendees = state.capacity.replace(".", "").toIntOrNull(),
                    imageUrls = uploadedImageUrls,
                    startDate = state.startDate.toTimeStamp(),
                    endDate = if (state.endDate.isNotEmpty()) state.endDate.toTimeStamp() else originalEvent.endDate
                )

                eventRepository.editEvent(eventId, updatedEvent)
                _editResult.value = CreateEventResult.Success
            } catch (e: Exception) {
                _editResult.value = CreateEventResult.Error(resourceProvider.getString(R.string.error_unknown) + ": ${e.message}")
            }
        }
    }

    private fun String.toTimeStamp(): Timestamp {
        val formats = listOf(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
            SimpleDateFormat("dd 'de' MMMM 'del' yyyy", Locale("es", "CO")),
            SimpleDateFormat("d 'de' MMMM 'del' yyyy", Locale("es", "CO"))
        )

        for (sdf in formats) {
            try {
                sdf.isLenient = false
                val date = sdf.parse(this)
                if (date != null) return Timestamp(date)
            } catch (e: Exception) { continue }
        }
        return Timestamp.now()
    }

    fun resetResult() {
        _editResult.value = CreateEventResult.Idle
    }
}
