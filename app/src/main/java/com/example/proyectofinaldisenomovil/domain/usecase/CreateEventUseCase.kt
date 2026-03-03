package com.example.proyectofinaldisenomovil.domain.usecase

import com.example.proyectofinaldisenomovil.data.model.Event.Event
import com.example.proyectofinaldisenomovil.data.model.ReputationReason
import com.example.proyectofinaldisenomovil.domain.repository.EventRepository
import com.example.proyectofinaldisenomovil.domain.repository.ReputationRepository
import javax.inject.Inject

/**
 * Creates a new event and awards [ReputationReason.EVENT_CREATED] points.
 *
 * Business rules enforced here:
 * - Title must not be blank.
 * - At least one image URL must be present.
 * - Coordinates must be valid (lat in [-90,90], lng in [-180,180]).
 */
class CreateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val reputationRepository: ReputationRepository,
) {
    sealed class Result {
        data class Success(val eventId: String) : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(event: Event): Result {
        if (event.title.isBlank())
            return Result.Error("El título del evento no puede estar vacío")
        if (event.imageUrls.isEmpty())
            return Result.Error("Debes agregar al menos una imagen")
        if (event.latitude !in -90.0..90.0 || event.longitude !in -180.0..180.0)
            return Result.Error("Coordenadas geográficas inválidas")

        return try {
            val id = eventRepository.createEvent(event)
            reputationRepository.addPoints(event.authorUid, ReputationReason.EVENT_CREATED, id)
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al crear el evento")
        }
    }
}
