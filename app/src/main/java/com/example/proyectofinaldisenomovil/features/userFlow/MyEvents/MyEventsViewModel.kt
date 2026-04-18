package com.example.proyectofinaldisenomovil.features.userFlow.MyEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyEventsUiState(
    val events: List<Event> = emptyList(),
    val filteredEvents: List<Event> = emptyList(),
    val selectedStatus: EventStatus = EventStatus.VERIFIED,
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)

@HiltViewModel
class MyEventsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyEventsUiState())
    val uiState: StateFlow<MyEventsUiState> = _uiState.asStateFlow()

    init {
        loadMyEvents()
    }

    fun loadMyEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUser = MockDataRepository.getLoggedInUser()
            if (currentUser != null) {
                val allEvents = eventRepository.getAllEvents()
                val myEvents = allEvents.filter { it.authorUid == currentUser.uid }
                _uiState.update { 
                    it.copy(
                        events = myEvents,
                        isLoading = false
                    )
                }
                applyFilters()
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onStatusChange(status: EventStatus) {
        _uiState.update { it.copy(selectedStatus = status) }
        applyFilters()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    private fun applyFilters() {
        _uiState.update { state ->
            val filtered = state.events.filter { event ->
                event.status == state.selectedStatus &&
                (state.searchQuery.isEmpty() || event.title.contains(state.searchQuery, ignoreCase = true))
            }
            state.copy(filteredEvents = filtered)
        }
    }

    fun refresh() {
        loadMyEvents()
    }
}
