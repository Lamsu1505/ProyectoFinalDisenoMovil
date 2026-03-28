package com.example.proyectofinaldisenomovil.domain.model

/**
 * Collectible badges a user can earn through participation.
 *
 * Stored as a [List]<[BadgeType]> inside the [com.example.proyectofinaldisenomovil.domain.model.User.User] document.
 *
 * @property label       Short badge name shown in the UI chip/card.
 * @property description Tooltip / achievement description.
 * @property iconRes     Resource name of the badge drawable (prefix "ic_badge_").
 */
enum class BadgeType(
    val label: String,
    val description: String,
    val iconRes: String,
) {
    PRIMERA_PUBLICACION(
        label       = "Primera Publicación",
        description = "Creaste tu primer evento",
        iconRes     = "ic_badge_first_event",
    ),
    DIEZ_PUBLICACIONES(
        label       = "Organizador Activo",
        description = "10 eventos verificados",
        iconRes     = "ic_badge_active_organizer",
    ),
    EVENTO_DESTACADO(
        label       = "Evento del Mes",
        description = "Tu evento fue el más destacado del mes",
        iconRes     = "ic_badge_top_event",
    ),
    COMENTARISTA(
        label       = "Comentarista",
        description = "Dejaste 50 comentarios",
        iconRes     = "ic_badge_commenter",
    ),
    ASISTENTE(
        label       = "Asistente Fiel",
        description = "Confirmaste asistencia a 20 eventos",
        iconRes     = "ic_badge_attendee",
    ),
}
