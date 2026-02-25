package com.example.proyectofinaldisenomovil.domain.usecase

import com.example.proyectofinaldisenomovil.data.model.User.UserRole
import com.example.proyectofinaldisenomovil.domain.repository.EventRepository
import com.example.proyectofinaldisenomovil.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Marks an event as Resuelto/Finalizado.
 *
 * Business rules:
 * - Only the event author OR a moderator can mark as resolved.
 */
class MarkEventResolvedUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository,
) {
    sealed class Result {
        object Success : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(
        eventId: String,
        eventAuthorUid: String,
        currentUserUid: String,
    ): Result {
        val user = userRepository.getUserById(currentUserUid)
            ?: return Result.Error("Usuario no encontrado")

        val canAct = currentUserUid == eventAuthorUid || user.role == UserRole.MODERATOR
        if (!canAct)
            return Result.Error("Solo el autor o un moderador puede resolver el evento")

        return try {
            eventRepository.markResolved(eventId)
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al marcar el evento como resuelto")
        }
    }
}
