package com.example.proyectofinaldisenomovil.features.userFlow.SavedEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.features.userFlow.LikedEvents.FavoriteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavedEventsUiState(
    val savedEvents: List<FavoriteEvent> = emptyList(),
    val categories: List<String> = listOf("Deportes", "Pasatiempo", "Academico"),
    val selectedCategory: String = "Deportes",
    val searchQuery: String = ""
)

@HiltViewModel
class SavedEventsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val attendanceRepository: AttendanceRepository
)
    : ViewModel() {
    private val _uiState = MutableStateFlow(SavedEventsUiState())
    val uiState: StateFlow<SavedEventsUiState> = _uiState.asStateFlow()

    init {
        loadSavedEvents()
    }

    private fun loadSavedEvents() {
        viewModelScope.launch {
            val currentUser = MockDataRepository.getLoggedInUser()
            if (currentUser != null) {
                val idSavedEvents = attendanceRepository.getEventsIdByUserID(currentUser.uid)
                val savedEvents = eventRepository.getEventsByIds(idSavedEvents)
                _uiState.value = _uiState.value.copy(
                    savedEvents = savedEvents.map { it.toFavoriteEvent() }
                )
            }
        }
    }

    private fun Event.toFavoriteEvent(): FavoriteEvent {
        return FavoriteEvent(
            id = this.id,
            title = this.title,
            category = this.category.label,
            date = this.startDate?.let {
                val dateFormat = java.text.SimpleDateFormat("EEEE d 'de' MMMM", java.util.Locale("es", "CO"))
                dateFormat.format(it.toDate())
            } ?: "",
            time = this.startDate?.let {
                val timeFormat = java.text.SimpleDateFormat("h:mm a", java.util.Locale("es", "CO"))
                timeFormat.format(it.toDate())
            } ?: "",
            location = this.address,
            distance = "",
            attendees = this.currentAttendees,
            imageUrl = this.thumbnailUrl
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelect(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onUnsaveEvent(eventId: String) {
        viewModelScope.launch {
            val currentUser = MockDataRepository.getLoggedInUser()
            currentUser?.let {
                MockDataRepository.toggleSaveEvent(it.uid, eventId)
                loadSavedEvents()
            }
        }
    }

    fun refresh() {
        loadSavedEvents()
    }
}