package com.example.proyectofinaldisenomovil.core.component.moderator.state

import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus

data class ModeratorPanelUiState(
    val events: List<Event> = emptyList(),
    val filteredEvents: List<Event> = emptyList(),
    val pendingEvents: List<Event> = emptyList(),
    val verifiedEvents: List<Event> = emptyList(),
    val rejectedEvents: List<Event> = emptyList(),
    val selectedCategory: EventCategory? = null,
    val statusFilter: EventStatus? = null,
    val searchQuery: String = "",
    val sortBy: SortOption = SortOption.NAME,
    val distanceKm: Int = 20,
    val isLoading: Boolean = true,
    val showLogoutDialog: Boolean = false,
    val showRejectDialog: Boolean = false,
    val eventToReject: Event? = null,
)

enum class SortOption(val label: String) {
    NAME("Nombre"),
    DATE("Fecha"),
    DISTANCE("Distancia"),
    VOTES("Votos"),
}