package com.example.proyectofinaldisenomovil.domain.usecase

import com.example.proyectofinaldisenomovil.data.model.AppNotification
import com.example.proyectofinaldisenomovil.data.model.NotificationType
import com.example.proyectofinaldisenomovil.data.model.ReputationReason
import com.example.proyectofinaldisenomovil.domain.repository.EventRepository
import com.example.proyectofinaldisenomovil.domain.repository.NotificationRepository
import com.example.proyectofinaldisenomovil.domain.repository.ReputationRepository
import javax.inject.Inject

/**
 * Moderator action: reject a PENDING_REVIEW event.
 *
 * Business rules:
 * - A rejection reason is mandatory.
 *
 * Side effects:
 * 1. Sets status → REJECTED and stores the reason.
 * 2. Deducts [ReputationReason.EVENT_REJECTED] points from the author.
 * 3. Sends an [AppNotification] with the reason to the author.
 */
class RejectEventUseCase @Inject constructor(
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
        reason: String,
    ): Result {
        if (reason.isBlank())
            return Result.Error("Debes ingresar un motivo de rechazo")

        return try {
            eventRepository.rejectEvent(eventId, moderatorUid, reason)
            reputationRepository.addPoints(eventAuthorUid, ReputationReason.EVENT_REJECTED, eventId)
            notificationRepository.sendNotification(
                AppNotification(
                    recipientUid = eventAuthorUid,
                    type         = NotificationType.EVENT_REJECTED,
                    title        = "Evento rechazado",
                    body         = "Tu evento \"$eventTitle\" fue rechazado. Motivo: $reason",
                    eventId      = eventId,
                )
            )
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al rechazar el evento")
        }
    }
}
