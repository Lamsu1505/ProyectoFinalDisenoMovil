package com.example.proyectofinaldisenomovil.data.model

/**
 * Lifecycle states of an [Event].
 *
 * Transition rules:
 *   PENDING_REVIEW → VERIFIED   (moderator approves)
 *   PENDING_REVIEW → REJECTED   (moderator rejects — must provide reason)
 *   VERIFIED       → RESOLVED   (author or moderator marks as finished)
 *
 * REJECTED events are hidden from the public feed.
 *
 * @property label Spanish label shown in status badges and the moderator panel.
 */
enum class EventStatus(val label: String) {
    PENDING_REVIEW("Pendiente de verificación"),
    VERIFIED("Verificado"),
    REJECTED("Rechazado"),
    RESOLVED("Resuelto / Finalizado"),
}
