package com.example.proyectofinaldisenomovil.domain.usecase

import com.example.proyectofinaldisenomovil.data.model.AppNotification
import com.example.proyectofinaldisenomovil.data.model.NotificationType
import com.example.proyectofinaldisenomovil.data.model.ReputationReason
import com.example.proyectofinaldisenomovil.domain.repository.EventRepository
import com.example.proyectofinaldisenomovil.domain.repository.NotificationRepository
import com.example.proyectofinaldisenomovil.domain.repository.ReputationRepository
import javax.inject.Inject

/**
 * Moderator action: approve a PENDING_REVIEW event.
 *
 * Side effects:
 * 1. Sets status → VERIFIED.
 * 2. Awards [ReputationReason.EVENT_VERIFIED] points to the author.
 * 3. Sends an [AppNotification] to the author.
 */
class VerifyEventUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val reputationRepository: ReputationRepository,
    private val notificationRepository: NotificationRepository,
) {
    sealed class Result {
        object Success : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(
        eventId: String,
        moderatorUid: String,
        eventAuthorUid: String,
        eventTitle: String,
    ): Result = try {
        eventRepository.verifyEvent(eventId, moderatorUid)
        reputationRepository.addPoints(eventAuthorUid, ReputationReason.EVENT_VERIFIED, eventId)
        notificationRepository.sendNotification(
            AppNotification(
                recipientUid = eventAuthorUid,
                type         = NotificationType.EVENT_VERIFIED,
                title        = "¡Evento verificado!",
                body         = "Tu evento \"$eventTitle\" fue aprobado por un moderador.",
                eventId      = eventId,
            )
        )
        Result.Success
    } catch (e: Exception) {
        Result.Error(e.message ?: "Error al verificar el evento")
    }
}
