package com.example.proyectofinaldisenomovil.data.repository

import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import kotlinx.coroutines.flow.Flow

/**
 * Contract for event CRUD and moderation operations.
 *
 * Implemented by [com.vivetuzona.data.repository.FirebaseEventRepository].
 */
interface EventRepository {

    /**
     * Real-time stream of VERIFIED (public) events, optionally filtered by
     * [category] and geographic [radiusKm] around [latLng].
     *
     * Geographic filtering uses a geohash bounding-box query; the final
     * precise distance check is done in-memory inside the repository.
     */
    fun observeFeedEvents(
        category: EventCategory? = null,
        latLng: Pair<Double, Double>? = null,
        radiusKm: Double = 10.0,
    ): Flow<List<Event>>

    /** Single document read — returns `null` when not found. */
    suspend fun getEventById(id: String): Event?

    /**
     * Persists a new event with [EventStatus.PENDING_REVIEW].
     * @return The Firestore-generated document ID.
     */
    suspend fun createEvent(event: Event): String

    /** Updates mutable fields of an existing event owned by the current user. */
    suspend fun updateEvent(event: Event)

    /** Hard-deletes an event. Only the author or a moderator may call this. */
    suspend fun deleteEvent(id: String)

    // ── Moderator actions ────────────────────────────────────────────────────

    /** Sets status to VERIFIED and records which moderator approved it. */
    suspend fun verifyEvent(id: String, moderatorUid: String)

    /** Sets status to REJECTED, stores the mandatory [reason], and hides the event. */
    suspend fun rejectEvent(id: String, moderatorUid: String, reason: String)

    /** Sets [Event.isResolved] = true. Can be called by author or moderator. */
    suspend fun markResolved(id: String)

    /** Returns all pending events for the moderator review panel. */
    fun observePendingEvents(): Flow<List<Event>>

    suspend fun getVerifiedEvents(): List<Event>
}
