package com.example.proyectofinaldisenomovil.data.repository.firebase

import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreEventRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : EventRepository {

    // TODO: permanent enforcement of moderator-only approval/rejection should be
    // backed by Firestore security rules in firestore.rules, not only by UI controls.

    private val events get() = db.collection(COLLECTION_EVENTS)

    override fun observeFeedEvents(
        category: EventCategory?,
        latLng: Pair<Double, Double>?,
        radiusKm: Double
    ): Flow<List<Event>> = flow {
        // TODO: realtime + geofilter. For now, emit a one-shot verified feed.
        emit(getVerifiedEvents())
    }

    override suspend fun getEventById(id: String): Event? {
        val snap = events.document(id).get().await()
        return snap.toEventOrNull()
    }

    override suspend fun updateEvent(event: Event) {
        require(event.id.isNotBlank()) { "Event.id is required" }
        events.document(event.id).set(event.toFirestoreMap()).await()
    }

    override suspend fun deleteEvent(id: String) {
        events.document(id).delete().await()
    }

    override suspend fun verifyEvent(id: String, moderatorUid: String) {
        events.document(id).update(
            mapOf(
                FIELD_STATUS to EventStatus.VERIFIED.name,
                FIELD_MODERATOR_UID to moderatorUid,
                FIELD_REJECTION_REASON to null,
                FIELD_UPDATED_AT to Timestamp.now(),
            )
        ).await()
    }

    override suspend fun rejectEvent(id: String, moderatorUid: String, reason: String) {
        events.document(id).update(
            mapOf(
                FIELD_STATUS to EventStatus.REJECTED.name,
                FIELD_MODERATOR_UID to moderatorUid,
                FIELD_REJECTION_REASON to reason,
                FIELD_UPDATED_AT to Timestamp.now(),
            )
        ).await()
    }

    override suspend fun markResolved(id: String) {
        events.document(id).update(
            mapOf(
                FIELD_IS_RESOLVED to true,
                FIELD_STATUS to EventStatus.RESOLVED.name,
                FIELD_UPDATED_AT to Timestamp.now(),
            )
        ).await()
    }

    override fun observePendingEvents(): Flow<List<Event>> = flow {
        val pending = events
            .whereEqualTo(FIELD_STATUS, EventStatus.PENDING_REVIEW.name)
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .get()
            .await()
            .documents
            .mapNotNull { it.toEventOrNull() }
        emit(pending)
    }

    override suspend fun getVerifiedEvents(): List<Event> {
        return events
            .whereEqualTo(FIELD_STATUS, EventStatus.VERIFIED.name)
            .whereEqualTo(FIELD_IS_RESOLVED, false)
            .orderBy(FIELD_START_DATE, Query.Direction.ASCENDING)
            .get()
            .await()
            .documents
            .mapNotNull { it.toEventOrNull() }
    }

    override suspend fun getEventsByIds(ids: List<String>): List<Event> {
        if (ids.isEmpty()) return emptyList()
        // Firestore "in" supports up to 10 items.
        return ids.chunked(10).flatMap { chunk ->
            events
                .whereIn(FIELD_ID, chunk)
                .get()
                .await()
                .documents
                .mapNotNull { it.toEventOrNull() }
        }
    }

    override suspend fun createEvent(
        title: String,
        description: String,
        category: EventCategory,
        address: String,
        imageUrls: List<String>,
        startDate: Timestamp,
        endDate: Timestamp,
        maxAttendees: Int?
    ): Event {
        val doc = events.document()
        val currentUser = auth.currentUser
        val created = Event(
            id = doc.id,
            authorUid = currentUser?.uid ?: "",
            authorName = currentUser?.displayName ?: "",
            title = title,
            description = description,
            category = category,
            address = address,
            imageUrls = imageUrls,
            maxAttendees = maxAttendees,
            currentAttendees = 0,
            status = EventStatus.PENDING_REVIEW,
            importantVotes = 0,
            isResolved = false,
            startDate = startDate,
            endDate = endDate,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        )
        doc.set(created.toFirestoreMap()).await()
        return created
    }

    override suspend fun getAllEvents(): List<Event> {
        return events
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .get()
            .await()
            .documents
            .mapNotNull { it.toEventOrNull() }
    }

    override suspend fun onEventAccept(event: Event) {
        verifyEvent(event.id, event.moderatorUid ?: "")
    }

    override suspend fun onEventReject(event: Event, reason: String) {
        rejectEvent(event.id, event.moderatorUid ?: "", reason)
    }

    override suspend fun editEvent(idEvent: String, newEvent: Event) {
        updateEvent(newEvent.copy(id = idEvent))
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toEventOrNull(): Event? {
        val id = getString(FIELD_ID) ?: this.id
        val title = getString(FIELD_TITLE) ?: return null
        val description = getString(FIELD_DESCRIPTION) ?: ""
        val category = (getString(FIELD_CATEGORY) ?: EventCategory.SOCIAL.name).let {
            runCatching { EventCategory.valueOf(it) }.getOrDefault(EventCategory.SOCIAL)
        }
        val status = (getString(FIELD_STATUS) ?: EventStatus.PENDING_REVIEW.name).let {
            runCatching { EventStatus.valueOf(it) }.getOrDefault(EventStatus.PENDING_REVIEW)
        }
        val imageUrls = (get(FIELD_IMAGE_URLS) as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

        return Event(
            id = id,
            authorUid = getString(FIELD_AUTHOR_UID) ?: "",
            authorName = getString(FIELD_AUTHOR_NAME) ?: "",
            title = title,
            description = description,
            category = category,
            imageUrls = imageUrls,
            latitude = getDouble(FIELD_LATITUDE) ?: 0.0,
            longitude = getDouble(FIELD_LONGITUDE) ?: 0.0,
            address = getString(FIELD_ADDRESS) ?: "",
            startDate = getTimestamp(FIELD_START_DATE),
            endDate = getTimestamp(FIELD_END_DATE),
            maxAttendees = (getLong(FIELD_MAX_ATTENDEES))?.toInt(),
            currentAttendees = (getLong(FIELD_CURRENT_ATTENDEES))?.toInt() ?: 0,
            status = status,
            moderatorUid = getString(FIELD_MODERATOR_UID),
            rejectionReason = getString(FIELD_REJECTION_REASON),
            importantVotes = (getLong(FIELD_IMPORTANT_VOTES))?.toInt() ?: 0,
            isResolved = getBoolean(FIELD_IS_RESOLVED) ?: false,
            aiSuggestedCategory = getString(FIELD_AI_SUGGESTED_CATEGORY),
            createdAt = getTimestamp(FIELD_CREATED_AT),
            updatedAt = getTimestamp(FIELD_UPDATED_AT),
        )
    }

    private fun Event.toFirestoreMap(): Map<String, Any?> = mapOf(
        FIELD_ID to id,
        FIELD_AUTHOR_UID to authorUid,
        FIELD_AUTHOR_NAME to authorName,
        FIELD_TITLE to title,
        FIELD_DESCRIPTION to description,
        FIELD_CATEGORY to category.name,
        FIELD_IMAGE_URLS to imageUrls,
        FIELD_LATITUDE to latitude,
        FIELD_LONGITUDE to longitude,
        FIELD_ADDRESS to address,
        FIELD_START_DATE to startDate,
        FIELD_END_DATE to endDate,
        FIELD_MAX_ATTENDEES to maxAttendees,
        FIELD_CURRENT_ATTENDEES to currentAttendees,
        FIELD_STATUS to status.name,
        FIELD_MODERATOR_UID to moderatorUid,
        FIELD_REJECTION_REASON to rejectionReason,
        FIELD_IMPORTANT_VOTES to importantVotes,
        FIELD_IS_RESOLVED to isResolved,
        FIELD_AI_SUGGESTED_CATEGORY to aiSuggestedCategory,
        FIELD_CREATED_AT to (createdAt ?: Timestamp.now()),
        FIELD_UPDATED_AT to Timestamp.now(),
    )

    private companion object {
        const val COLLECTION_EVENTS = "events"

        const val FIELD_ID = "id"
        const val FIELD_AUTHOR_UID = "authorUid"
        const val FIELD_AUTHOR_NAME = "authorName"
        const val FIELD_TITLE = "title"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_CATEGORY = "category"
        const val FIELD_IMAGE_URLS = "imageUrls"
        const val FIELD_LATITUDE = "latitude"
        const val FIELD_LONGITUDE = "longitude"
        const val FIELD_ADDRESS = "address"
        const val FIELD_START_DATE = "startDate"
        const val FIELD_END_DATE = "endDate"
        const val FIELD_MAX_ATTENDEES = "maxAttendees"
        const val FIELD_CURRENT_ATTENDEES = "currentAttendees"
        const val FIELD_STATUS = "status"
        const val FIELD_MODERATOR_UID = "moderatorUid"
        const val FIELD_REJECTION_REASON = "rejectionReason"
        const val FIELD_IMPORTANT_VOTES = "importantVotes"
        const val FIELD_IS_RESOLVED = "isResolved"
        const val FIELD_AI_SUGGESTED_CATEGORY = "aiSuggestedCategory"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
}

