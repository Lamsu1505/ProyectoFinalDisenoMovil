package com.example.proyectofinaldisenomovil.features.userFlow.SavedEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
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
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val selectedOrder: String = "Nombre",
    val eventToAddToCalendar: FavoriteEvent? = null
)

@HiltViewModel
class SavedEventsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val attendanceRepository: AttendanceRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var allSavedEvents: List<Event> = emptyList()
    private val _uiState = MutableStateFlow(SavedEventsUiState())
    val uiState: StateFlow<SavedEventsUiState> = _uiState.asStateFlow()

    init {
        loadSavedEvents()
    }

    private fun loadSavedEvents() {
        viewModelScope.launch {
            val currentUser = userRepository.getLoggedInUser()
            if (currentUser != null) {
                val idSavedEvents = attendanceRepository.getEventsIdByUserID(currentUser.uid)
                allSavedEvents = eventRepository.getEventsByIds(idSavedEvents)
                applyFilters()
            }
        }
    }

    private fun applyFilters() {
        val currentState = _uiState.value
        var filtered = allSavedEvents

        if (currentState.selectedCategory != null) {
            filtered = filtered.filter { it.category.label == currentState.selectedCategory }
        }

        if (currentState.searchQuery.isNotBlank()) {
            filtered = filtered.filter { it.title.contains(currentState.searchQuery, ignoreCase = true) }
        }

        filtered = when (currentState.selectedOrder) {
            "Nombre" -> filtered.sortedBy { it.title }
            "Fecha" -> filtered.sortedBy { it.startDate }
            "Popularidad" -> filtered.sortedByDescending { it.currentAttendees }
            else -> filtered
        }

        _uiState.value = _uiState.value.copy(savedEvents = filtered.map { it.toFavoriteEvent() })
    }

    fun onCategorySelect(category: String?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilters()
    }

    fun onOrderSelect(order: String) {
        _uiState.value = _uiState.value.copy(selectedOrder = order)
        applyFilters()
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
            imageUrl = this.thumbnailUrl,
            startTimeMillis = this.startDate?.toDate()?.time,
            endTimeMillis = this.endDate?.toDate()?.time
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun onUnsaveEvent(eventId: String) {
        viewModelScope.launch {
            val currentUser = userRepository.getLoggedInUser()
            currentUser?.let {
                attendanceRepository.cancelAttendance(eventId, it.uid)
                loadSavedEvents()
            }
        }
    }

    fun refresh() {
        loadSavedEvents()
    }

    fun onAddToCalendar(event: FavoriteEvent) {
        _uiState.value = _uiState.value.copy(eventToAddToCalendar = event)
    }

    fun onCalendarEventHandled() {
        _uiState.value = _uiState.value.copy(eventToAddToCalendar = null)
    }
}
