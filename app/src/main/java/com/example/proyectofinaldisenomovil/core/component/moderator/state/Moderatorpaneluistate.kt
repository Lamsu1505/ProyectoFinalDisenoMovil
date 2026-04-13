package com.example.proyectofinaldisenomovil.core.component.moderator.state

import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory

/**
 * Immutable UI state for [com.vivetuzona.ui.screens.moderator.ModeratorPanelScreen].
 *
 * @param events           Full list of pending events fetched from Firestore.
 * @param filteredEvents   Events after applying [selectedCategory] and [searchQuery].
 * @param selectedCategory Active category chip filter, null = show all.
 * @param searchQuery      Current text in the search bar.
 * @param sortBy           Current sort mode label (e.g. "Nombre", "Fecha").
 * @param distanceKm       Current distance filter in km.
 * @param isLoading        True while the initial data load is in progress.
 * @param showLogoutDialog True when the logout confirmation dialog should be visible.
 */
data class ModeratorPanelUiState(
    val events: List<Event> = emptyList(),
    val filteredEvents: List<Event> = emptyList(),
    val selectedCategory: EventCategory? = null,
    val searchQuery: String = "",
    val sortBy: SortOption = SortOption.NAME,
    val distanceKm: Int = 20,
    val isLoading: Boolean = true,
    val showLogoutDialog: Boolean = false,
    val showRejectDialog: Boolean = false,
    val eventToReject: Event? = null,
)

/**
 * Sort options available in the "Ordenar por" dropdown.
 * @property label Spanish label shown in the UI chip.
 */
enum class SortOption(val label: String) {
    NAME("Nombre"),
    DATE("Fecha"),
    DISTANCE("Distancia"),
    VOTES("Votos"),
}