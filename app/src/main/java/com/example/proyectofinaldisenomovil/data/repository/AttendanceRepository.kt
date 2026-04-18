package com.example.proyectofinaldisenomovil.data.repository

import com.example.proyectofinaldisenomovil.domain.model.Attendance
import com.example.proyectofinaldisenomovil.domain.model.User.User
import kotlinx.coroutines.flow.Flow

/**
 * Contract for attendance (RSVP) persistence.
 *
 * Implemented by [com.vivetuzona.data.repository.FirebaseAttendanceRepository].
 */
interface AttendanceRepository {

    /**
     * Creates an [Attendance] record and increments [Event.currentAttendees].
     * Must check [Event.hasCapacity] before calling.
     */
    suspend fun confirmAttendance(eventId: String, uid: String)

    /**
     * Deletes the [Attendance] record and decrements [Event.currentAttendees].
     */
    suspend fun cancelAttendance(eventId: String, uid: String)

    /** Returns `true` if the user has already confirmed attendance. */
    suspend fun isAttending(eventId: String, uid: String): Boolean

    /** Returns current confirmed attendee count for display purposes. */
    suspend fun getAttendeeCount(eventId: String): Int

    /** Real-time stream of all attendances for a given user (for the profile screen). */
    fun observeUserAttendances(uid: String): Flow<List<Attendance>>

    fun getEventsIdByUserID(uid: String) : List<String>
    fun fetchAttendances(): List<Attendance>
}
