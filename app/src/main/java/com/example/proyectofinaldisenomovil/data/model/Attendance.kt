package com.example.proyectofinaldisenomovil.data.model

//import com.google.firebase.Timestamp
import java.security.Timestamp

/**
 * Records a user's confirmed attendance for an event.
 *
 * Firestore collection : "attendances"
 * Document ID          : "{eventId}_{uid}"
 *                        (composite key prevents duplicate confirmations)
 *
 * When created, [com.example.proyectofinaldisenomovil.data.model.Event.Event.currentAttendees] must be incremented atomically
 * using `FieldValue.increment(1)`.
 */
data class Attendance(
    /** Composite key: "{eventId}_{uid}" */
    val id: String = "",
    val eventId: String = "",
    val uid: String = "",
    val confirmedAt: Timestamp? = null,
)
