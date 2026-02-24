package com.example.proyectofinaldisenomovil.data.model.Event

/**
 * Available categories for a community event.
 *
 * @property label Spanish display label used in UI chips and filter dropdowns.
 */
enum class EventCategory(val label: String) {
    DEPORTES("Deportes"),
    CULTURA("Cultura"),
    ACADEMICO("Académico"),
    VOLUNTARIADO("Voluntariado"),
    SOCIAL("Social"),
}
