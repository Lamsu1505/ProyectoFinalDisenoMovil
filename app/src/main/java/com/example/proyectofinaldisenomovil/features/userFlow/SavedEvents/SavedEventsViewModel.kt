package com.example.proyectofinaldisenomovil.features.userFlow.SavedEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.features.userFlow.LikedEvents.FavoriteEvent
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Locale

data class SavedEventsUiState(
    val savedEvents: List<FavoriteEvent> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: EventCategory? = null,
    val searchQuery: String = "",
    val selectedOrder: String = "",
    val eventToAddToCalendar: FavoriteEvent? = null
)

@HiltViewModel
class SavedEventsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val attendanceRepository: AttendanceRepository,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private var allSavedEvents: List<Event> = emptyList()
    private val _uiState = MutableStateFlow(SavedEventsUiState())
    val uiState: StateFlow<SavedEventsUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            categories = listOf(
                resourceProvider.getString(R.string.category_sports),
                resourceProvider.getString(R.string.category_hobby),
                resourceProvider.getString(R.string.category_academic)
            ),
            selectedOrder = resourceProvider.getString(R.string.filter_name)
        )
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
            filtered = filtered.filter { it.category == currentState.selectedCategory }
        }

        if (currentState.searchQuery.isNotBlank()) {
            filtered = filtered.filter { it.title.contains(currentState.searchQuery, ignoreCase = true) }
        }

        filtered = when (currentState.selectedOrder) {
            resourceProvider.getString(R.string.filter_name) -> filtered.sortedBy { it.title }
            resourceProvider.getString(R.string.filter_date) -> filtered.sortedBy { it.startDate }
            resourceProvider.getString(R.string.filter_popularity) -> filtered.sortedByDescending { it.currentAttendees }
            else -> filtered
        }

        _uiState.value = _uiState.value.copy(savedEvents = filtered.map { it.toFavoriteEvent() })
    }

    fun onCategorySelect(category: EventCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilters()
    }

    fun onOrderSelect(order: String) {
        _uiState.value = _uiState.value.copy(selectedOrder = order)
        applyFilters()
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            selectedCategory = null,
            searchQuery = "",
            selectedOrder = resourceProvider.getString(R.string.filter_name)
        )
        applyFilters()
    }

    private fun Event.toFavoriteEvent(): FavoriteEvent {
        val currentLocale = Locale.getDefault()
        return FavoriteEvent(
            id = this.id,
            title = this.title,
            category = this.category.label,
            date = this.startDate?.let {
                val dateFormat = java.text.SimpleDateFormat(resourceProvider.getString(R.string.date_format_full), currentLocale)
                dateFormat.format(it.toDate())
            } ?: "",
            time = this.startDate?.let {
                val timeFormat = java.text.SimpleDateFormat(resourceProvider.getString(R.string.time_format), currentLocale)
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
