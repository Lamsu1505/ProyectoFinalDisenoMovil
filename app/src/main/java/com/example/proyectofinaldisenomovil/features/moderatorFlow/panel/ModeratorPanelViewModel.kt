package com.example.proyectofinaldisenomovil.features.moderatorFlow.panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.core.component.moderator.state.ModeratorPanelUiState
import com.example.proyectofinaldisenomovil.core.component.moderator.state.SortOption
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ModeratorPanelViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ModeratorPanelUiState())
    val uiState: StateFlow<ModeratorPanelUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
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
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun onEventAccept(event: Event) {
        // TODO: Implement accept event logic
    }

    fun onEventReject(event: Event) {
        // TODO: Implement reject event logic
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // TODO: Load events from repository
            // For now, using empty list - will be populated when connected to data

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun applyFilters() {
        val currentState = _uiState.value
        var filtered = currentState.events

        // Filter by category
        if (currentState.selectedCategory != null) {
            filtered = filtered.filter { it.category == currentState.selectedCategory }
        }

        // Filter by search query
        if (currentState.searchQuery.isNotBlank()) {
            filtered = filtered.filter { event ->
                event.title.contains(currentState.searchQuery, ignoreCase = true) ||
                event.description.contains(currentState.searchQuery, ignoreCase = true) ||
                event.address.contains(currentState.searchQuery, ignoreCase = true)
            }
        }

        // Apply sorting
        filtered = when (currentState.sortBy) {
            SortOption.NAME -> filtered.sortedBy { it.title }
            SortOption.DATE -> filtered.sortedBy { it.startDate }
            SortOption.DISTANCE -> filtered.sortedBy { it.address }
            SortOption.VOTES -> filtered.sortedByDescending { it.currentAttendees } //TODO arreglar esta linea de filtros
        }

        _uiState.update { it.copy(filteredEvents = filtered) }
    }
}
