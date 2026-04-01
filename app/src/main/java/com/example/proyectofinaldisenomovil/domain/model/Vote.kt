package com.example.proyectofinaldisenomovil.domain.model

//import com.google.firebase.Timestamp
import java.security.Timestamp

/**
 * Represents an "Es importante / Me interesa" vote cast by a user on an event.
 *
 * Firestore collection : "votes"
 * Document ID          : "{eventId}_{uid}"
 *                        (composite key ensures one vote per user per event)
 *
 * When created, [com.example.proyectofinaldisenomovil.domain.model.Event.Event.importantVotes] must be incremented atomically.
 * When deleted, [com.example.proyectofinaldisenomovil.domain.model.Event.Event.importantVotes] must be decremented atomically.
 */
data class Vote(
    /** Composite key: "{eventId}_{uid}" */
    val id: String = "",
    val eventId: String = "",
    val uid: String = "",
    val createdAt: Timestamp? = null,
)
