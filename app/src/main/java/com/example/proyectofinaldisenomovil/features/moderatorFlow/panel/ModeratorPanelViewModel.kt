package com.example.proyectofinaldisenomovil.features.moderatorFlow.panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.core.component.moderator.state.ModeratorPanelUiState
import com.example.proyectofinaldisenomovil.core.component.moderator.state.SortOption
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ModeratorPanelViewModel @Inject constructor() : ViewModel() {

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
        // TODO: Implement accept event logic - call repository.verifyEvent()
    }

    fun onEventReject(event: Event) {
        // TODO: Implement reject event logic - call repository.rejectEvent()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Simulating network delay
            delay(800)

            // Sample events for testing
            val sampleEvents = listOf(
                Event(
                    id = "event-1",
                    title = "Concierto de Rock en el Parque",
                    description = "Disfruta de una noche llena de música y energía con las mejores bandas de rock locales. ¡No te lo pierdas!",
                    category = EventCategory.CULTURA,
                    startDate = Timestamp(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)),
                    endDate = Timestamp(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000)),
                    address = "Parque Central, Av. Principal 123, Ciudad",
                    imageUrls = listOf("https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?w=800"),
                    currentAttendees = 45,
                    maxAttendees = 200,
                ),
                Event(
                    id = "event-2",
                    title = "Maratón Ciudad 2026",
                    description = "Únete a la carrera más grande del año. Categorías para todos los niveles.",
                    category = EventCategory.DEPORTES,
                    startDate = Timestamp(Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000)),
                    endDate = Timestamp(Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000 + 4 * 60 * 60 * 1000)),
                    address = "Estadio Municipal, Calle Sport 456",
                    imageUrls = listOf("https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=800"),
                    currentAttendees = 120,
                    maxAttendees = 500,
                ),
                Event(
                    id = "event-3",
                    title = "Feria de Gastronomía Local",
                    description = "Degusta los mejores platos de la región. Talleres de cocina y presentaciones en vivo.",
                    category = EventCategory.SOCIAL,
                    startDate = Timestamp(Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000)),
                    endDate = Timestamp(Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000 + 6 * 60 * 60 * 1000)),
                    address = "Plaza del Pueblo, Centro Histórico",
                    imageUrls = listOf("https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800"),
                    currentAttendees = 80,
                    maxAttendees = 150,
                ),
                Event(
                    id = "event-4",
                    title = "Conferencia de Tecnología",
                    description = "Expertos tecnológicos de todo el país hablando sobre IA, blockchain y el futuro.",
                    category = EventCategory.DIVERSION,
                    startDate = Timestamp(Date(System.currentTimeMillis() + 21 * 24 * 60 * 60 * 1000)),
                    endDate = Timestamp(Date(System.currentTimeMillis() + 21 * 24 * 60 * 60 * 1000 + 8 * 60 * 60 * 1000)),
                    address = "Centro de Convenciones, Torre Innovation",
                    imageUrls = listOf("https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800"),
                    currentAttendees = 200,
                    maxAttendees = 300,
                ),
                Event(
                    id = "event-5",
                    title = "Exposición de Arte Moderno",
                    description = "Obras de artistas emergentes y reconocidos. Inauguración con vino de honor.",
                    category = EventCategory.CULTURA,
                    startDate = Timestamp(Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000)),
                    endDate = Timestamp(Date(System.currentTimeMillis() + 12 * 24 * 60 * 60 * 1000)),
                    address = "Museo de Arte Contemporáneo, Av. Cultura 789",
                    imageUrls = listOf("https://images.unsplash.com/photo-1531243269054-5ebf6f34081e?w=800"),
                    currentAttendees = 35,
                    maxAttendees = 100,
                ),
            )

            _uiState.update {
                it.copy(
                    events = sampleEvents,
                    filteredEvents = sampleEvents,
                    isLoading = false,
                )
            }
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
            SortOption.VOTES -> filtered.sortedByDescending { it.currentAttendees }
        }

        _uiState.update { it.copy(filteredEvents = filtered) }
    }
}
