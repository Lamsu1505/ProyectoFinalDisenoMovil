package com.example.proyectofinaldisenomovil.domain.model

/**
 * Defines every action that awards or deducts reputation points.
 *
 * @property points Point delta applied to [com.example.proyectofinaldisenomovil.domain.model.User.User.reputationPoints].
 *                  Positive = reward, negative = penalty.
 * @property label  Human-readable Spanish description for the history screen.
 */
enum class ReputationReason(val points: Int, val label: String) {
    // Crear eventos
    EVENT_CREATED(10,    "Evento creado"),
    EVENT_VERIFIED(20,   "Evento verificado por moderador"),
    
    // Participación social
    COMMENT_ADDED(5,     "Comentario agregado"),
    ATTENDANCE_CONFIRMED(3, "Asistencia a evento confirmada"),
    
    // Votos y popularidad
    VOTE_RECEIVED(2,     "Voto 'Es importante' recibido"),
    VOTE_POSITIVE_RECEIVED(1, "Voto positivo recibido"),
    
    // Interacciones adicionales
    EVENT_SAVED(1,       "Evento guardado en favoritos"),
    PROFILE_COMPLETED(5, "Perfil completado"),
    DAILY_LOGIN(1,       "Inicio de sesión diario"),
    
    // Penalizaciones
    EVENT_REJECTED(-5,   "Evento rechazado por moderador"),
    EVENT_CANCELLED(-3,  "Evento cancelado por organizador"),
    CONTENT_REPORTED(-2, "Reporte de contenido falso"),
}



