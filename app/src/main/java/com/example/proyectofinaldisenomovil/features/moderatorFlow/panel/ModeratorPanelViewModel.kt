package com.example.proyectofinaldisenomovil.features.moderatorFlow.panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.core.component.moderator.state.ModeratorPanelUiState
import com.example.proyectofinaldisenomovil.core.component.moderator.state.SortOption
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModeratorPanelViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ModeratorPanelUiState())
    val uiState: StateFlow<ModeratorPanelUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val pendingEvents = MockDataRepository.getPendingEvents()
            
            _uiState.update {
                it.copy(
                    events = pendingEvents,
                    filteredEvents = pendingEvents,
                    isLoading = false
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onCategorySelect(category: EventCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFilters()
    }

    fun onSortChange(sortOption: SortOption) {
        _uiState.update { it.copy(sortBy = sortOption) }
        applyFilters()
    }

    fun onDistanceChange(distanceKm: Int) {
        _uiState.update { it.copy(distanceKm = distanceKm) }
        applyFilters()
    }

    fun onLogoutClick() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onLogoutDismiss() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun onLogoutConfirm() {
        MockDataRepository.logout()
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun onEventAccept(event: Event) {
        MockDataRepository.approveEvent(event.id)
        loadEvents()
    }

    fun onEventReject(event: Event) {
        _uiState.update { it.copy(showRejectDialog = true, eventToReject = event) }
    }

    fun onRejectDialogDismiss() {
        _uiState.update { it.copy(showRejectDialog = false, eventToReject = null) }
    }

    fun onRejectConfirm(reason: String) {
        val event = _uiState.value.eventToReject ?: return
        MockDataRepository.rejectEvent(event.id, reason)
        _uiState.update { it.copy(showRejectDialog = false, eventToReject = null) }
        loadEvents()
    }

    private fun applyFilters() {
        val currentState = _uiState.value
        var filtered = currentState.events

        currentState.selectedCategory?.let { category ->
            filtered = filtered.filter { it.category == category }
        }

        if (currentState.searchQuery.isNotBlank()) {
            filtered = filtered.filter { event ->
                event.title.contains(currentState.searchQuery, ignoreCase = true) ||
                event.description.contains(currentState.searchQuery, ignoreCase = true) ||
                event.address.contains(currentState.searchQuery, ignoreCase = true)
            }
        }

        filtered = when (currentState.sortBy) {
            SortOption.NAME -> filtered.sortedBy { it.title }
            SortOption.DATE -> filtered.sortedBy { it.startDate }
            SortOption.DISTANCE -> filtered.sortedBy { it.address }
            SortOption.VOTES -> filtered.sortedByDescending { it.importantVotes }
        }

        _uiState.update { it.copy(filteredEvents = filtered) }
    }

    fun refresh() {
        loadEvents()
    }
}
