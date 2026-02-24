package com.example.proyectofinaldisenomovil.data.model

/**
 * Defines every action that awards or deducts reputation points.
 *
 * @property points Point delta applied to [com.example.proyectofinaldisenomovil.data.model.User.User.reputationPoints].
 *                  Positive = reward, negative = penalty.
 * @property label  Human-readable Spanish description for the history screen.
 */
enum class ReputationReason(val points: Int, val label: String) {
    EVENT_CREATED(10,    "Evento creado"),
    EVENT_VERIFIED(20,   "Evento verificado por moderador"),
    COMMENT_ADDED(5,     "Comentario agregado"),
    VOTE_RECEIVED(2,     "Voto \"Es importante\" recibido"),
    ATTENDANCE_CONFIRMED(3, "Asistencia a evento confirmada"),
    EVENT_REJECTED(-5,   "Evento rechazado por moderador"),
}
