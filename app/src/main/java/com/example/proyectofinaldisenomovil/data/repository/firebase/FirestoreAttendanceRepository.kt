package com.example.proyectofinaldisenomovil.data.repository.firebase

import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.domain.model.Attendance
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreAttendanceRepository @Inject constructor(
    private val db: FirebaseFirestore,
) : AttendanceRepository {

    override suspend fun confirmAttendance(eventId: String, uid: String) {
        val attendanceRef = db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_ATTENDANCES).document(uid)
        val eventRef = db.collection(COLLECTION_EVENTS).document(eventId)

        db.runTransaction { tx ->
            val existing = tx.get(attendanceRef)
            if (!existing.exists()) {
                tx.set(
                    attendanceRef,
                    mapOf(
                        FIELD_UID to uid,
                        FIELD_EVENT_ID to eventId,
                        FIELD_CONFIRMED_AT to Timestamp.now(),
                    )
                )
                tx.update(eventRef, FIELD_CURRENT_ATTENDEES, FieldValue.increment(1))
            }
            null
        }.await()
    }

    override suspend fun cancelAttendance(eventId: String, uid: String) {
        val attendanceRef = db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_ATTENDANCES).document(uid)
        val eventRef = db.collection(COLLECTION_EVENTS).document(eventId)

        db.runTransaction { tx ->
            val existing = tx.get(attendanceRef)
            if (existing.exists()) {
                tx.delete(attendanceRef)
                tx.update(eventRef, FIELD_CURRENT_ATTENDEES, FieldValue.increment(-1))
            }
            null
        }.await()
    }

    override suspend fun isAttending(eventId: String, uid: String): Boolean {
        val snap = db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_ATTENDANCES).document(uid)
            .get()
            .await()
        return snap.exists()
    }

    override suspend fun getAttendeeCount(eventId: String): Int {
        val snap = db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_ATTENDANCES)
            .get()
            .await()
        return snap.size()
    }

    override fun observeUserAttendances(uid: String): Flow<List<Attendance>> = flow {
        // TODO: realtime snapshot listener. For now, emit one-shot.
        emit(fetchUserAttendances(uid))
    }

    private suspend fun fetchUserAttendances(uid: String): List<Attendance> {
        val query = db.collectionGroup(SUBCOLLECTION_ATTENDANCES)
            .whereEqualTo(FIELD_UID, uid)
            .get()
            .await()
        return query.documents.map { doc ->
            Attendance(
                id = doc.id,
                uid = doc.getString(FIELD_UID) ?: uid,
                eventId = doc.getString(FIELD_EVENT_ID) ?: "",
                confirmedAt = doc.getTimestamp(FIELD_CONFIRMED_AT),
            )
        }.filter { it.eventId.isNotBlank() }
    }

    override suspend fun getEventsIdByUserID(uid: String): List<String> {
        return fetchUserAttendances(uid).map { it.eventId }.distinct()
    }

    override fun fetchAttendances(): List<Attendance> {
        // Legacy sync API.
        return emptyList()
    }

    private companion object {
        const val COLLECTION_EVENTS = "events"
        const val SUBCOLLECTION_ATTENDANCES = "attendances"

        const val FIELD_UID = "uid"
        const val FIELD_EVENT_ID = "eventId"
        const val FIELD_CONFIRMED_AT = "confirmedAt"

        const val FIELD_CURRENT_ATTENDEES = "currentAttendees"
    }
}

