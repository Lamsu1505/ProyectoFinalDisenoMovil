package com.example.proyectofinaldisenomovil.domain.usecase

import com.example.proyectofinaldisenomovil.data.model.ReputationReason
import com.example.proyectofinaldisenomovil.domain.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.domain.repository.EventRepository
import com.example.proyectofinaldisenomovil.domain.repository.ReputationRepository
import javax.inject.Inject

/**
 * Confirms or cancels a user's attendance at an event.
 *
 * Business rules:
 * - Cannot confirm if [Event.hasCapacity] is false.
 *
 * Side effects on confirm:
 * - Increments [Event.currentAttendees].
 * - Awards [ReputationReason.ATTENDANCE_CONFIRMED] points to the user.
 */
class ConfirmAttendanceUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val eventRepository: EventRepository,
    private val reputationRepository: ReputationRepository,
) {
    sealed class Result {
        object Confirmed : Result()
        object Cancelled : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(eventId: String, uid: String): Result {
        val event = eventRepository.getEventById(eventId)
            ?: return Result.Error("Evento no encontrado")

        val alreadyAttending = attendanceRepository.isAttending(eventId, uid)

        return if (alreadyAttending) {
            attendanceRepository.cancelAttendance(eventId, uid)
            Result.Cancelled
        } else {
            if (!event.hasCapacity)
                return Result.Error("Este evento ya no tiene cupo disponible")
            attendanceRepository.confirmAttendance(eventId, uid)
            reputationRepository.addPoints(uid, ReputationReason.ATTENDANCE_CONFIRMED, eventId)
            Result.Confirmed
        }
    }
}
